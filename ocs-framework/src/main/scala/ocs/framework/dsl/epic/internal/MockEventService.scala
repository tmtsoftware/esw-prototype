package ocs.framework.dsl.epic.internal

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, Source, SourceQueueWithComplete}
import akka.stream.{ActorMaterializer, KillSwitch, KillSwitches, Materializer, OverflowStrategy}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}

case class MockEvent(key: String, value: Any)

class MockEventService {

  var map: Map[String, MockEvent]                                          = Map.empty
  var subscriptions: Map[String, List[SourceQueueWithComplete[MockEvent]]] = Map.empty

  implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())
  implicit val actorSystem: ActorSystem            = ActorSystem("server")
  implicit val mat: Materializer                   = ActorMaterializer()

  def get(key: String): Future[Option[MockEvent]] = Future {
    map.get(key)
  }

  def publish(key: String, value: MockEvent): Future[Unit] = Future.unit.flatMap { _ =>
    map = map + (key -> value)
    Future.traverse(subscriptions.getOrElse(key, List.empty))(_.offer(value)).map(_ => ())
  }

  def subscribe(key: String): Source[MockEvent, KillSwitch] = {
    val (queue: SourceQueueWithComplete[MockEvent], stream) =
      Source.queue[MockEvent](1024, OverflowStrategy.dropHead).preMaterialize()
    Future {
      subscriptions = subscriptions + (key -> (queue :: subscriptions.getOrElse(key, List.empty)))
    }
    stream.viaMat(KillSwitches.single)(Keep.right)
  }

}
