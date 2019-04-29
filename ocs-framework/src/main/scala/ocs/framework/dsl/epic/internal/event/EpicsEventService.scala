package ocs.framework.dsl.epic.internal.event

import akka.stream.KillSwitch
import akka.stream.scaladsl.Source
import play.api.libs.json.{Format, Json, OFormat}
import romaine.codec.RomaineStringCodec

import scala.concurrent.Future

case class EpicsEvent[T: Format](key: String, value: T)

object EpicsEvent {

  implicit def eventJson[T: Format]: OFormat[EpicsEvent[T]] = Json.format

  implicit def mockEventRomaineCodec[T: Format]: RomaineStringCodec[EpicsEvent[T]] = new RomaineStringCodec[EpicsEvent[T]] {
    override def toString(x: EpicsEvent[T]): String = eventJson.writes(x).toString()

    override def fromString(string: String): EpicsEvent[T] = eventJson.reads(Json.parse(string)).get
  }
}

trait EpicsEventService {

  def get[T: Format](key: String): Future[Option[EpicsEvent[T]]]

  def publish[T: Format](key: String, value: EpicsEvent[T]): Future[Unit]

  def subscribe[T: Format](key: String): Source[EpicsEvent[T], KillSwitch]

}
