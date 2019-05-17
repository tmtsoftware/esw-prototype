package ocs.framework.dsl.epic.internal.event

import akka.Done
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Keep, Source, SourceQueueWithComplete}
import ocs.framework.dsl.FutureUtils
import sequencer.macros.StrandEc

import scala.concurrent.duration.DurationLong
import scala.concurrent.{ExecutionContext, Future}

case class MockEvent(key: String, params: Map[String, Any]) {
  def add(field: String, value: Any): MockEvent = copy(params = params + (field -> value))
}

object MockEvent {
  def empty(key: String): MockEvent = MockEvent(key, Map.empty)
}

class MockEventService {

  var database: Map[String, MockEvent]                                     = Map.empty
  var subscriptions: Map[String, List[SourceQueueWithComplete[MockEvent]]] = Map.empty

  implicit val strandEc: StrandEc       = StrandEc.create()
  implicit val ec: ExecutionContext     = strandEc.ec
  implicit val actorSystem: ActorSystem = ActorSystem("server")
  implicit val mat: Materializer        = ActorMaterializer()

  def get(key: String): Future[MockEvent] = FutureUtils.timeout(2.seconds, strandEc.executorService).map { _ =>
    database.getOrElse(key, MockEvent.empty(key))
  }

  def publish(key: String, field: String, value: Any): Future[Done] =
    FutureUtils.timeout(1.seconds, strandEc.executorService).flatMap { _ =>
      val event    = database.getOrElse(key, MockEvent.empty(key))
      val newEvent = event.add(field, value)
      database = database + (key -> newEvent)
      Future.traverse(subscriptions.getOrElse(key, List.empty))(_.offer(newEvent)).map(_ => Done)
    }

  def subscribe(key: String): Source[MockEvent, KillSwitch] = {
    val (queue: SourceQueueWithComplete[MockEvent], stream) =
      Source.queue[MockEvent](1024, OverflowStrategy.dropHead).preMaterialize()

    Future {
      val list = subscriptions.getOrElse(key, List.empty)
      subscriptions = subscriptions + (key -> (queue :: list))
    }

    stream.viaMat(KillSwitches.single)(Keep.right)
  }

}
