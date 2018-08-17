package tmt.sequencer

import java.net.URI
import java.nio.ByteBuffer

import com.typesafe.config.ConfigFactory
import io.lettuce.core.codec.Utf8StringCodec
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands
import io.lettuce.core.{RedisClient, RedisURI}
import romaine.reactive.{RedisKeySpaceCodec, RedisPSubscribeScalaApi, RedisSubscriptionApi}
import tmt.sequencer.RedisConnectionCodec.StringCodec
import ujson.Js
import upickle.default.{read, write, writeJs, _}

import scala.compat.java8.FutureConverters.CompletionStageOps
import scala.concurrent.{ExecutionContext, Future}

class RedisConnectionFactory(locationServiceWrapper: LocationServiceGateway)(implicit ec: ExecutionContext) {

  lazy val masterId: String = ConfigFactory.load().getString("csw-event.redis.masterId")

  lazy val redisURI: Future[URI] = locationServiceWrapper.redisUrI

  def redisClient: Future[RedisClient] = redisURI.map { uri =>
    RedisClient.create(RedisURI.Builder.sentinel(uri.getHost, uri.getPort, masterId).build())
  }

  def reactiveConnection[K, V](redisConCodec: RedisConnectionCodec[K, V]): Future[RedisPubSubReactiveCommands[K, V]] =
    redisURI.flatMap { redisUri =>
      redisClient.flatMap { client =>
        client
          .connectPubSubAsync(redisConCodec, RedisURI.create(redisUri))
          .toScala
          .map(_.reactive())
      }
    }

  def wrappedReactiveConnection[K, V](redisConCodec: RedisConnectionCodec[K, V]): Future[RedisPSubscribeScalaApi[K, V]] =
    reactiveConnection(redisConCodec).map(new RedisPSubscribeScalaApi(_))

  lazy val redisSubscriptionApi: Future[RedisSubscriptionApi[String, String]] =
    wrappedReactiveConnection(StringCodec)
      .map { redisPSubscribeApi =>
        new RedisSubscriptionApi(() => redisPSubscribeApi)
      }
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
