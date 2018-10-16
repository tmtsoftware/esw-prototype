package ocs.gateway

import akka.stream.scaladsl.Source
import com.typesafe.config.ConfigFactory
import ocs.factory.LocationServiceWrapper
import reactor.core.publisher.FluxSink.OverflowStrategy
import romaine.RomaineFactory
import romaine.reactive.{RedisSubscription, RedisSubscriptionApi}

class SequencerMonitor(locationServiceWrapper: LocationServiceWrapper, romaineFactory: RomaineFactory) {

  private lazy val masterId: String = ConfigFactory.load().getString("csw-event.redis.masterId")

  private lazy val redisSubscriptionApi: RedisSubscriptionApi[String, String] = {
    romaineFactory.redisSubscriptionApi(locationServiceWrapper.redisUrI(masterId))
  }

  def watch(sequencerId: String, observingMode: String): Source[String, RedisSubscription] = {
    redisSubscriptionApi
      .subscribe(List(s"$sequencerId-$observingMode"), OverflowStrategy.LATEST)
      .map(_.value)
  }
}
