package tmt.ocs.models
import java.time.{Clock, Instant}

object InstantNow {
  def time: Instant = {
    Instant.now(Clock.systemUTC())
  }
}
