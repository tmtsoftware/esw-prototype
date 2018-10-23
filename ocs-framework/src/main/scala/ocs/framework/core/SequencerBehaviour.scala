package ocs.framework.core
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.{ActorSystem, Scheduler}
import akka.util.Timeout
import csw.command.client.messages.CommandResponseManagerMessage
import csw.command.client.messages.CommandResponseManagerMessage.AddOrUpdateCommand
import csw.params.commands.CommandResponse
import csw.params.commands.CommandResponse.SubmitResponse
import ocs.api.messages.SequencerMsg
import ocs.api.messages.SequencerMsg._
import ocs.api.models.{AggregateResponse, Step, StepList, StepStatus}
import ocs.framework.ScriptImports.toDuration

import scala.util.{Failure, Success, Try}

object SequencerBehaviour {
  def behavior(crmRef: ActorRef[CommandResponseManagerMessage])(implicit system: ActorSystem): Behavior[SequencerMsg] =
    Behaviors.setup { ctx =>
      val crmMapper: ActorRef[SubmitResponse] = ctx.messageAdapter(rsp ⇒ Update(rsp))

      var stepRefOpt: Option[ActorRef[Step]]                       = None
      var sequence: StepList                                       = StepList.empty
      var responseRefOpt: Option[ActorRef[Try[AggregateResponse]]] = None
      var aggregateResponse: AggregateResponse                     = AggregateResponse()

      def sendNext(replyTo: ActorRef[Step]): Unit = sequence.next match {
        case Some(step) => setInFlight(replyTo, step)
        case None       => stepRefOpt = Some(replyTo)
      }

      def trySend(): Unit = {
        for {
          ref  <- stepRefOpt
          step <- sequence.next
        } {
          setInFlight(ref, step)
          stepRefOpt = None
        }
      }

      def setInFlight(replyTo: ActorRef[Step], step: Step): Unit = {
        implicit val timeout: Timeout     = Timeout(10.seconds)
        implicit val scheduler: Scheduler = system.scheduler
        val inFlightStep                  = step.withStatus(StepStatus.InFlight)
        sequence = sequence.updateStep(inFlightStep)
        replyTo ! inFlightStep
        val runId = step.command.runId
        crmRef ! AddOrUpdateCommand(runId, CommandResponse.Started(runId))
        crmRef ! CommandResponseManagerMessage.Subscribe(runId, crmMapper)
      }

      def update(_submitResponse: SubmitResponse): Unit = {
        sequence = sequence.updateStatus(Set(_submitResponse.runId), StepStatus.Finished)
        aggregateResponse = aggregateResponse.merge(AggregateResponse(_submitResponse))
        clearSequenceIfFinished()
      }

      def clearSequenceIfFinished(): Unit = {
        if (sequence.isFinished) {
          println("Sequence is finished")
          responseRefOpt.foreach(x => x ! Success(aggregateResponse))
          sequence = StepList.empty
          aggregateResponse = AggregateResponse()
          responseRefOpt = None
          stepRefOpt = None
        }
      }

      def updateAndSendResponse(newSequence: StepList, replyTo: ActorRef[Try[Unit]]): Unit = {
        sequence = newSequence
        clearSequenceIfFinished()
        replyTo ! Success({})
      }

      Behaviors.receiveMessage[SequencerMsg] { msg =>
        if (sequence.isFinished) {
          msg match {
            case ProcessSequence(Nil, replyTo)      => replyTo ! Failure(new RuntimeException("empty sequence can not be processed"))
            case ProcessSequence(commands, replyTo) => sequence = StepList.from(commands); responseRefOpt = Some(replyTo)
            case GetSequence(replyTo)               => replyTo ! Success(sequence)
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
            case GetSequence(replyTo)               => replyTo ! Success(sequence)
            case GetNext(replyTo)                   => sendNext(replyTo)
            case MaybeNext(replyTo)                 => replyTo ! sequence.next
            case Update(_aggregateResponse)         => update(_aggregateResponse)
            case Add(commands, replyTo)             => updateAndSendResponse(sequence.append(commands), replyTo)
            case Pause(replyTo)                     => updateAndSendResponse(sequence.pause, replyTo)
            case Resume(replyTo)                    => updateAndSendResponse(sequence.resume, replyTo)
            case DiscardPending(replyTo)            => updateAndSendResponse(sequence.discardPending, replyTo)
            case Replace(stepId, commands, replyTo) => updateAndSendResponse(sequence.replace(stepId, commands), replyTo)
            case Prepend(commands, replyTo)         => updateAndSendResponse(sequence.prepend(commands), replyTo)
            case Delete(ids, replyTo)               => updateAndSendResponse(sequence.delete(ids.toSet), replyTo)
            case InsertAfter(id, commands, replyTo) => updateAndSendResponse(sequence.insertAfter(id, commands), replyTo)
            case AddBreakpoints(ids, replyTo)       => updateAndSendResponse(sequence.addBreakpoints(ids), replyTo)
            case RemoveBreakpoints(ids, replyTo)    => updateAndSendResponse(sequence.removeBreakpoints(ids), replyTo)
          }
        }
        trySend()
        Behaviors.same
      }
    }
}
