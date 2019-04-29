package ocs.framework.dsl.epic.internal.event

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, Source, SourceQueueWithComplete}
import akka.stream.{ActorMaterializer, KillSwitch, KillSwitches, Materializer, OverflowStrategy}
import ocs.framework.dsl.epic.internal.event.EpicsEvent.eventJson
import play.api.libs.json.{Format, Json}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}

// Don't look inside!
class MockEventService extends EpicsEventService {

  var map: Map[String, String]                                          = Map.empty
  var subscriptions: Map[String, List[SourceQueueWithComplete[String]]] = Map.empty

  implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())
  implicit val actorSystem: ActorSystem            = ActorSystem("server")
  implicit val mat: Materializer                   = ActorMaterializer()

  def get[T: Format](key: String): Future[Option[EpicsEvent[T]]] = Future {
    map.get(key).map(str => eventJson.reads(Json.parse(str)).get)
  }

  def publish[T: Format](key: String, value: EpicsEvent[T]): Future[Unit] = Future.unit.flatMap { _ =>
    val str = eventJson.writes(value).toString()
    map = map + (key -> str)
    Future.traverse(subscriptions.getOrElse(key, List.empty))(_.offer(str)).map(_ => ())
  }

  def subscribe[T: Format](key: String): Source[EpicsEvent[T], KillSwitch] = {
    val (queue: SourceQueueWithComplete[String], stream) =
      Source.queue[String](1024, OverflowStrategy.dropHead).preMaterialize()

    Future {
      val list = subscriptions.getOrElse(key, List.empty)
      subscriptions = subscriptions + (key -> (queue :: list))
    }

    stream
      .map(str => eventJson.reads(Json.parse(str)).get)
      .viaMat(KillSwitches.single)(Keep.right)
  }

}
