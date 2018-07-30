package tmt.sequencer.rpc.server

import java.nio.file.{Files, Paths}
import java.util.Base64

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.Source

import scala.compat.java8.StreamConverters._
import scala.concurrent.ExecutionContext

class WavefrontImages(implicit mat: Materializer, ec: ExecutionContext) {
  private val prefix = "data:image/jpeg;base64,"

  def imageContentAsUrls: Source[String, NotUsed] = {
    val imagePaths = Files.list(Paths.get("/usr/local/data/frames")).toScala[List]
    Source(imagePaths.sorted).map(path => prefix + Base64.getEncoder.encodeToString(Files.readAllBytes(path)))
  }
}
