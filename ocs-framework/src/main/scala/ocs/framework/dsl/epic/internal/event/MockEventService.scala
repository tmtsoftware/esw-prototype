package ocs.framework.dsl.epic.internal.event

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Keep, Source, SourceQueueWithComplete}
import ocs.framework.dsl.FutureUtils
import sequencer.macros.StrandEc

import scala.concurrent.duration.DurationLong
import scala.concurrent.{ExecutionContext, Future}

case class EpicsEvent(key: String, value: Any)

class MockEventService {

  var map: Map[String, EpicsEvent]                                          = Map.empty
  var subscriptions: Map[String, List[SourceQueueWithComplete[EpicsEvent]]] = Map.empty

  implicit val strandEc: StrandEc       = StrandEc.create()
  implicit val ec: ExecutionContext     = strandEc.ec
  implicit val actorSystem: ActorSystem = ActorSystem("server")
  implicit val mat: Materializer        = ActorMaterializer()

  def get(key: String): Future[Option[EpicsEvent]] = FutureUtils.delay(8.seconds) {
    Future {
      map.get(key)
    }
  }

  def publish(key: String, value: EpicsEvent): Future[Unit] =
    FutureUtils.delay(4.seconds, strandEc.executorService).flatMap { _ =>
      map = map + (key -> value)
      Future.traverse(subscriptions.getOrElse(key, List.empty))(_.offer(value)).map(_ => ())
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
