package tmt.sequencer

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.typesafe.config.ConfigFactory
import io.lettuce.core.RedisURI
import reactor.core.publisher.FluxSink.OverflowStrategy
import romaine.RomaineFactory
import romaine.reactive.RedisSubscriptionApi

import scala.async.Async.{async, await}
import scala.concurrent.{ExecutionContext, Future}

class SequencerMonitor(locationServiceWrapper: LocationServiceGateway, romaineFactory: RomaineFactory)(
    implicit ec: ExecutionContext
) {

  private lazy val masterId: String = ConfigFactory.load().getString("csw-event.redis.masterId")

  private lazy val redisSubscriptionApi: Future[RedisSubscriptionApi[String, String]] = async {
    val uri: RedisURI = await(locationServiceWrapper.redisUrI(masterId))
    await(romaineFactory.redisSubscriptionApi(uri))
  }

  def watch(sequencerId: String, observingMode: String): Source[String, NotUsed] = {
    Source
      .fromFuture(redisSubscriptionApi)
      .flatMapConcat { api =>
        api
          .subscribe(List(s"$sequencerId-$observingMode"), OverflowStrategy.LATEST)
          .map(_.value)
      }
  }
}
