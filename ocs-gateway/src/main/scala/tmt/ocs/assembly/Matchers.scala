package tmt.ocs.assembly

import akka.util.Timeout
import csw.command.models.matchers.DemandMatcher
import csw.messages.params.generics.KeyType.IntKey
import csw.messages.params.models.Prefix
import csw.messages.params.states.{DemandState, StateName}

import scala.concurrent.duration.DurationInt

object Matchers {

  val FilterMatcher = DemandMatcher(
    DemandState(
      Prefix("nfiraos.sample1assembly"),
      StateName("filter"),
      Set(IntKey.make("filter-position").set(8))
    ),
    withUnits = true,
    Timeout(100.seconds)
  )

  val DisperserMatcher = DemandMatcher(
    DemandState(
      Prefix("nfiraos.sample1assembly"),
      StateName("disperser"),
      Set(IntKey.make("disperser-position").set(12))
    ),
    withUnits = true,
    Timeout(100.seconds)
  )

}
