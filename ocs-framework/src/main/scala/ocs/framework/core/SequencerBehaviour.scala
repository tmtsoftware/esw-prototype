package ocs.framework.core
import akka.Done
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import csw.command.client.messages.CommandResponseManagerMessage
import csw.command.client.messages.CommandResponseManagerMessage.{AddOrUpdateCommand, AddSubCommand, UpdateSubCommand}
import csw.params.commands.CommandResponse
import csw.params.commands.CommandResponse.SubmitResponse
import csw.params.core.models.Id
import ocs.api.messages.SequencerMsg
import ocs.api.messages.SequencerMsg._
import ocs.api.models._

import scala.util.{Failure, Success, Try}

object SequencerBehaviour {
  def behavior(crmRef: ActorRef[CommandResponseManagerMessage]): Behavior[SequencerMsg] =
    Behaviors.setup { ctx =>
      val crmMapper: ActorRef[SubmitResponse] = ctx.messageAdapter(rsp ⇒ Update(rsp))

      var stepRefOpt: Option[ActorRef[Step]]                    = None
      var readyToExecuteNextRefOpt: Option[ActorRef[Done]]      = None
      var stepList: StepList                                    = StepList.empty
      var responseRefOpt: Option[ActorRef[Try[SubmitResponse]]] = None
      var latestResponse: Option[SubmitResponse]                = None
      val emptyChildId                                          = Id("empty-child")

      def sendNext(replyTo: ActorRef[Step]): Unit = stepList.next match {
        case Some(step) => setInFlight(replyTo, step)
        case None       => stepRefOpt = Some(replyTo)
      }

      def readyToExecuteNext(replyTo: ActorRef[Done]): Unit = {
        if (!stepList.isInFlight) {
          replyTo ! Done
        } else {
          readyToExecuteNextRefOpt = Some(replyTo)
        }
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
        val stepRunId = step.command.runId
        crmRef ! AddSubCommand(stepList.runId, stepRunId)
        crmRef ! AddOrUpdateCommand(CommandResponse.Started(stepRunId))
        crmRef ! CommandResponseManagerMessage.Subscribe(stepRunId, crmMapper)
        replyTo ! inFlightStep
      }

      def isSequenceFinished: Boolean = {
        val isSequenceCrmFinal = latestResponse match {
          case Some(res) => res.runId.equals(stepList.runId)
          case None      => false
        }
        stepList.isFinished || isSequenceCrmFinal
      }

      def update(_submitResponse: SubmitResponse): Unit = {
        //why 2 level nesting for line below
        crmRef ! UpdateSubCommand(CommandResponse.withRunId(_submitResponse.runId, _submitResponse))
        stepList = stepList.updateStatus(Set(_submitResponse.runId), StepStatus.Finished)
        latestResponse = Some(_submitResponse)
        clearIfSequenceFinished()
        readyToExecuteNextRefOpt.foreach(x => readyToExecuteNext(x))
      }

      def clearIfSequenceFinished(): Unit = {
        if (isSequenceFinished) {
          println("Sequence is finished")
          val sequenceResponse = CommandResponse.withRunId(stepList.runId, latestResponse.orNull) //whether this will be called with None latestresponse ever??
          crmRef ! UpdateSubCommand(CommandResponse.withRunId(emptyChildId, sequenceResponse))
          responseRefOpt.foreach(x => x ! Success(sequenceResponse))
          stepList = StepList.empty
          readyToExecuteNextRefOpt.foreach(x => readyToExecuteNext(x))
          latestResponse = None
          responseRefOpt = None
          readyToExecuteNextRefOpt = None
        }
      }

      def updateAndSendResponse(newSequence: StepList, replyTo: ActorRef[Try[Unit]]): Unit = {
        stepList = newSequence
        clearIfSequenceFinished()
        replyTo ! Success({})
      }

      def processSequence(sequence: Sequence, replyTo: ActorRef[Try[SubmitResponse]]): Unit = {
        val runId = sequence.runId
        stepList = StepList.from(sequence)
        crmRef ! AddOrUpdateCommand(CommandResponse.Started(runId))
        crmRef ! CommandResponseManagerMessage.Subscribe(runId, crmMapper)
        crmRef ! AddSubCommand(runId, emptyChildId)
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
            case ReadyToExecuteNext(replyTo)        => readyToExecuteNext(replyTo)
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
            case ReadyToExecuteNext(replyTo)        => readyToExecuteNext(replyTo)
          }
        }
        trySend()
        Behaviors.same
      }
    }
}
