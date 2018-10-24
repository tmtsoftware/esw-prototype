package ocs.framework.core
import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import csw.command.client.messages.CommandResponseManagerMessage
import csw.command.client.messages.CommandResponseManagerMessage.{AddOrUpdateCommand, AddSubCommand}
import csw.params.commands.CommandResponse
import csw.params.commands.CommandResponse.SubmitResponse
import ocs.api.messages.SequencerMsg
import ocs.api.messages.SequencerMsg._
import ocs.api.models._

import scala.util.{Failure, Success, Try}

object SequencerBehaviour {
  def behavior(crmRef: ActorRef[CommandResponseManagerMessage])(implicit system: ActorSystem): Behavior[SequencerMsg] =
    Behaviors.setup { ctx =>
      val crmMapper: ActorRef[SubmitResponse] = ctx.messageAdapter(rsp ⇒ Update(rsp))

      var stepRefOpt: Option[ActorRef[Step]]                    = None
      var stepList: StepList                                    = StepList.empty
      var responseRefOpt: Option[ActorRef[Try[SubmitResponse]]] = None
      var sequenceResponse: SubmitResponse                      = null

      def sendNext(replyTo: ActorRef[Step]): Unit = stepList.next match {
        case Some(step) => setInFlight(replyTo, step)
        case None       => stepRefOpt = Some(replyTo)
      }

      def trySend(): Unit = {
        for {
          ref  <- stepRefOpt
          step <- stepList.next
        } {
          setInFlight(ref, step)
          stepRefOpt = None
        }
      }

      def setInFlight(replyTo: ActorRef[Step], step: Step): Unit = {
        val inFlightStep = step.withStatus(StepStatus.InFlight)
        stepList = stepList.updateStep(inFlightStep)
        replyTo ! inFlightStep
        val runId = step.command.runId
        crmRef ! AddSubCommand(stepList.runId, runId)
        crmRef ! AddOrUpdateCommand(runId, CommandResponse.Started(runId))
        crmRef ! CommandResponseManagerMessage.Subscribe(runId, crmMapper)
      }

      def update(_submitResponse: SubmitResponse): Unit = {
        stepList = stepList.updateStatus(Set(_submitResponse.runId), StepStatus.Finished)
        if (stepList.isFinished) {
          sequenceResponse = _submitResponse
        }
        clearSequenceIfFinished()
      }

      def clearSequenceIfFinished(): Unit = {
        if (stepList.isFinished) {
          println("Sequence is finished")
          responseRefOpt.foreach(x => x ! Success(sequenceResponse))
          stepList = StepList.empty
          sequenceResponse = null
          responseRefOpt = None
          stepRefOpt = None
        }
      }

      def updateAndSendResponse(newSequence: StepList, replyTo: ActorRef[Try[Unit]]): Unit = {
        stepList = newSequence
        clearSequenceIfFinished()
        replyTo ! Success({})
      }

      def processSequence(sequence: Sequence, replyTo: ActorRef[Try[SubmitResponse]]): Unit = {
        val runId = sequence.runId
        stepList = StepList.from(runId, sequence.commands.toList)
        crmRef ! AddOrUpdateCommand(runId, CommandResponse.Started(runId))
        crmRef ! CommandResponseManagerMessage.Subscribe(runId, crmMapper)
        responseRefOpt = Some(replyTo)
      }

      Behaviors.receiveMessage[SequencerMsg] { msg =>
        if (stepList.isFinished) {
          msg match {
            case ProcessSequence(null, replyTo) =>
              replyTo ! Failure(new RuntimeException("empty sequence can not be processed"))
            case ProcessSequence(sequence, replyTo) => processSequence(sequence, replyTo)
            case GetSequence(replyTo)               => replyTo ! Success(stepList)
            case GetNext(replyTo)                   => sendNext(replyTo)
            case x: ExternalSequencerMsg =>
              x.replyTo ! Failure(
                new RuntimeException(s"${x.getClass.getSimpleName} can not be applied on a finished sequence")
              )
            case x => println(s"${x.getClass.getSimpleName} can not be applied on a finished sequence")
          }
        } else {
          msg match {
            case ProcessSequence(_, replyTo)        => replyTo ! Failure(new RuntimeException("previous sequence has not finished yet"))
            case GetSequence(replyTo)               => replyTo ! Success(stepList)
            case GetNext(replyTo)                   => sendNext(replyTo)
            case MaybeNext(replyTo)                 => replyTo ! stepList.next
            case Update(_submitResponse)            => update(_submitResponse)
            case Add(commands, replyTo)             => updateAndSendResponse(stepList.append(commands), replyTo)
            case Pause(replyTo)                     => updateAndSendResponse(stepList.pause, replyTo)
            case Resume(replyTo)                    => updateAndSendResponse(stepList.resume, replyTo)
            case DiscardPending(replyTo)            => updateAndSendResponse(stepList.discardPending, replyTo)
            case Replace(stepId, commands, replyTo) => updateAndSendResponse(stepList.replace(stepId, commands), replyTo)
            case Prepend(commands, replyTo)         => updateAndSendResponse(stepList.prepend(commands), replyTo)
            case Delete(ids, replyTo)               => updateAndSendResponse(stepList.delete(ids.toSet), replyTo)
            case InsertAfter(id, commands, replyTo) => updateAndSendResponse(stepList.insertAfter(id, commands), replyTo)
            case AddBreakpoints(ids, replyTo)       => updateAndSendResponse(stepList.addBreakpoints(ids), replyTo)
            case RemoveBreakpoints(ids, replyTo)    => updateAndSendResponse(stepList.removeBreakpoints(ids), replyTo)
          }
        }
        trySend()
        Behaviors.same
      }
    }
}
