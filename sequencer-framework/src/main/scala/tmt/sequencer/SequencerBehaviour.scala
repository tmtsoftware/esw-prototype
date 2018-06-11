package tmt.sequencer

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import tmt.sequencer.messages.SequencerMsg
import tmt.sequencer.messages.SequencerMsg._
import tmt.sequencer.models._

import scala.util.{Failure, Success, Try}

object SequencerBehaviour {
  def behavior: Behavior[SequencerMsg] = Behaviors.setup { _ =>
    var stepRefOpt: Option[ActorRef[Step]]                       = None
    var sequence: Sequence                                       = Sequence.empty
    var responseRefOpt: Option[ActorRef[Try[AggregateResponse]]] = None
    var aggregateResponse: AggregateResponse                     = AggregateResponse

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
      val inFlightStep = step.withStatus(StepStatus.InFlight)
      sequence = sequence.updateStep(inFlightStep)
      replyTo ! inFlightStep
    }

    def update(_aggregateResponse: AggregateResponse): Unit = {
      sequence = sequence.updateStatus(_aggregateResponse.ids, StepStatus.Finished)
      aggregateResponse = aggregateResponse.add(_aggregateResponse)
      if (sequence.isFinished) {
        println("Sequence is finished")
        responseRefOpt.foreach(x => x ! Success(aggregateResponse))
        sequence = Sequence.empty
        aggregateResponse = AggregateResponse
        responseRefOpt = None
        stepRefOpt = None
      }
    }

    Behaviors.immutable { (_, msg) =>
      if (sequence.isFinished) {
        msg match {
          case ProcessSequence(Nil, replyTo)      => replyTo ! Failure(new RuntimeException("empty sequence can not be processed"))
          case ProcessSequence(commands, replyTo) => sequence = Sequence.from(commands); responseRefOpt = Some(replyTo)
          case GetSequence(replyTo)               => replyTo ! sequence
          case GetNext(replyTo)                   => sendNext(replyTo)
          case x                                  => println(s"command=$x can not be applied on a finished sequence")
        }
      } else {
        msg match {
          case ProcessSequence(_, replyTo) => replyTo ! Failure(new RuntimeException("previous sequence has not finished yet"))
          case GetSequence(replyTo)        => replyTo ! sequence
          case GetNext(replyTo)            => sendNext(replyTo)
          case MaybeNext(replyTo)          => replyTo ! sequence.next
          case Update(_aggregateResponse)  => update(_aggregateResponse)
          case Add(commands)               => sequence = sequence.append(commands)
          case Pause                       => sequence = sequence.pause
          case Resume                      => sequence = sequence.resume
          case DiscardPending              => sequence = sequence.discardPending
          case Replace(stepId, commands)   => sequence = sequence.replace(stepId, commands)
          case Prepend(commands)           => sequence = sequence.prepend(commands)
          case Delete(ids)                 => sequence = sequence.delete(ids.toSet)
          case InsertAfter(id, commands)   => sequence = sequence.insertAfter(id, commands)
          case AddBreakpoints(ids)         => sequence = sequence.addBreakpoints(ids)
          case RemoveBreakpoints(ids)      => sequence = sequence.removeBreakpoints(ids)
        }
      }
      trySend()
      Behaviors.same
    }
  }
}
