package tmt.sequencer
import java.net.URI
import java.nio.ByteBuffer

import com.typesafe.config.ConfigFactory
import io.lettuce.core.api.async.RedisAsyncCommands
import io.lettuce.core.codec.Utf8StringCodec
import io.lettuce.core.{RedisClient, RedisURI}
import romaine.RedisAsyncScalaApi
import romaine.reactive.RedisKeySpaceCodec
import tmt.sequencer.util.LocationServiceGateway
import ujson.Js
import upickle.default.{read, write, writeJs, _}

import scala.compat.java8.FutureConverters.CompletionStageOps
import scala.concurrent.{ExecutionContext, Future}

class RedisConnectionFactory(locationServiceWrapper: LocationServiceGateway)(implicit ec: ExecutionContext) {

  lazy val masterId = ConfigFactory.load().getString("csw-event.redis.masterId")

  lazy val redisURI: Future[URI] = locationServiceWrapper.redisUrI

  def redisClient: Future[RedisClient] = redisURI.map { uri =>
    RedisClient.create(RedisURI.Builder.sentinel(uri.getHost, uri.getPort, masterId).build())
  }

  def asyncConnection[K, V](redisConnectionCodec: RedisConnectionCodec[K, V]): Future[RedisAsyncCommands[K, V]] =
    redisURI.flatMap(
      redisUri ⇒ redisClient.flatMap(_.connectAsync(redisConnectionCodec, RedisURI.create(redisUri)).toScala.map(_.async()))
    )

  def wrappedAsyncConnection[K, V](redisConnectionCodec: RedisConnectionCodec[K, V]): Future[RedisAsyncScalaApi[K, V]] =
    asyncConnection(redisConnectionCodec).map(new RedisAsyncScalaApi(_))
}

class RedisConnectionCodec[K: ReadWriter, V: ReadWriter] extends RedisKeySpaceCodec[K, V] {
  private lazy val utf8StringCodec = new Utf8StringCodec()

  override def encodeKey(key: K): ByteBuffer     = utf8StringCodec.encodeKey(toKeyString(key))
  override def decodeKey(byteBuf: ByteBuffer): K = fromKeyString(utf8StringCodec.decodeKey(byteBuf))

  override def encodeValue(value: V): ByteBuffer   = utf8StringCodec.encodeValue(write(value))
  override def decodeValue(byteBuf: ByteBuffer): V = read[V](utf8StringCodec.decodeValue(byteBuf))

  override def toKeyString(key: K): String         = writeJs(key).str
  override def fromKeyString(keyString: String): K = read[K](Js.Str(keyString))
}

object RedisConnectionCodec {
  implicit object StringCodec extends RedisConnectionCodec[String, String]
}
