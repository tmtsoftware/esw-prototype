package ocs.framework.dsl.epic.internal.event

import akka.Done
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Keep, Source, SourceQueueWithComplete}
import ocs.framework.dsl.FutureUtils
import sequencer.macros.StrandEc

import scala.concurrent.duration.DurationLong
import scala.concurrent.{ExecutionContext, Future}

case class EpicsEvent(key: String, params: Map[String, Any])
object EpicsEvent {
  def empty(key: String): EpicsEvent = EpicsEvent(key, Map.empty)
}

class MockEventService {

  var map: Map[String, EpicsEvent]                                          = Map.empty
  var subscriptions: Map[String, List[SourceQueueWithComplete[EpicsEvent]]] = Map.empty

  implicit val strandEc: StrandEc       = StrandEc.create()
  implicit val ec: ExecutionContext     = strandEc.ec
  implicit val actorSystem: ActorSystem = ActorSystem("server")
  implicit val mat: Materializer        = ActorMaterializer()

  def get(key: String): Future[EpicsEvent] = FutureUtils.timeout(4.seconds, strandEc.executorService).map { _ =>
    map.getOrElse(key, EpicsEvent.empty(key))
  }

  def publish(value: EpicsEvent): Future[Done] =
    FutureUtils.timeout(2.seconds, strandEc.executorService).flatMap { _ =>
      map = map + (value.key -> value)
      Future.traverse(subscriptions.getOrElse(value.key, List.empty))(_.offer(value)).map(_ => Done)
    }

  def subscribe(key: String): Source[EpicsEvent, KillSwitch] = {
    val (queue: SourceQueueWithComplete[EpicsEvent], stream) =
      Source.queue[EpicsEvent](1024, OverflowStrategy.dropHead).preMaterialize()

    Future {
      val list = subscriptions.getOrElse(key, List.empty)
      subscriptions = subscriptions + (key -> (queue :: list))
    }

    stream.viaMat(KillSwitches.single)(Keep.right)
  }

}
