package ocs.framework.dsl.epic.internal

import akka.stream.KillSwitch
import akka.stream.scaladsl.Sink
import ocs.framework.dsl.epic.internal.event.EpicsEvent
import play.api.libs.json.Format

import scala.concurrent.Future

class Var[T: Format](init: T) {
  @volatile
  private var _value = init
  def set(x: T): Unit = {
    _value = x
  }

  def :=(x: T): Unit = set(x)
  def get: T         = _value

  override def toString: String = _value.toString
}

class ProcessVar[T: Format](init: T, key: String)(implicit mc: Machine[_]) extends Var[T](init) {

  import mc.{ec, eventService, mat}

  /*
    pvStat pvPut(channel ch, compType mode = DEFAULT, double timeout = 10.0)

    Puts (or writes) the value of ch to the process variable it has been assigned to.
    Returns the status from the PV layer.

    By default, i.e. with no optional arguments, pvPut is un-confirmed “fire and forget”; completion must be inferred
    by other means. An optional second argument can change this default:
    • SYNC causes it to block the state set until completion. This mode is called synchronous.
    • ASYNC allows the state set to continue but still check for completion via a subsequent call to pvPutComplete (typically in a condition). This mode is called asnchronous.
    A timeout value may be specified after the SYNC argument. This should be a positive floating point number, specifying the number of seconds before the request times out.
    This value overrides the default timeout of 10 seconds.

    Note that SNL allows only one pending pvPut per variable and state set to be active.
    As long as a pvPut(var,ASYNC) is pending completion, further calls to pvPut(var,ASYNC) from the same
    state set immediately fail and an error message is printed; whereas further calls to pvPut(var,SYNC)
    are delayed until the previous operation completes.
   */
  def pvPut(): Future[Unit] = {
    val value = get
    Future.unit.flatMap { _ =>
      eventService.publish(key, EpicsEvent(key, value))
    }
  }

  /*
    pvStat pvGet(channel ch, compType ct = DEFAULT, double timeout = 10.0)

    Gets (or reads) the value of ch from the process variable it has been assigned to. Returns the status
    from the PV layer.

    Like for pvPut, only one pending pvGet per channel and state set can be active.
   */
  def pvGet(): Future[Unit] = {
    eventService.get(key).map { option =>
      option.foreach { event =>
        set(event.value)
        mc.refresh("pvGet")
      }
    }
  }

  def monitor(): KillSwitch = {
    eventService
      .subscribe(key)
      .mapAsync(1) { event =>
        Future.unit.flatMap { _ =>
          set(event.value)
          mc.refresh("monitor")
        }
      }
      .to(Sink.ignore)
      .run()
  }

  /*
    pvStat pvAssign(channel ch, char *pv_name)

    Assigns or re-assigns ch to a process variable with name pv_name. If pv_name is an empty string or NULL,
    then ch is de-assigned (not associated with any process variable)

    Note that pvAssign is asynchronous: it sends a request to search for and connect to the given pv_name,
    but it does not wait for a response, similar to pvGet(var,ASYNC). Calling pvAssign does have one
    immediate effect, namely de-assigning the variable from any PV it currently is assigned to. In order
    to make sure that it has connected to the new PV, you can use the pvConnected built-in function inside
    a transition clause.
   */
  def pvAssign() = ???

  /*
    int  delay (delay_in_seconds)
    float  delay_in_seconds;

    The delay function returns TRUE if the specified time has elapsed from entering the state.
    It should be used only within a when expression.
   */
  def delay = ???

  /*
    pvStat pvMonitor(channel ch)

    Initiates a monitor on the process variable that ch was assigned to.
   */
  def pvMonitor = ???

  /*
    pvStat pvStopMonitor(channel ch)

    Terminates a monitor on the underlying process variable.
   */
  def pvStopMonitor = ???

  /*
    void pvPutCancel(channel ch)

    Cancel a pending (asynchronous) pvPut.
   */
  def pvPutCancel = ???

  /*
    seqBool pvPutComplete(channel ch)

    Returns whether the last asynchronous pvPut to this process variable has completed.
   */
  def pvPutComplete = ???

  /*
    seqBool pvGetComplete(channel ch)

    Returns whether the last asynchronous pvGet for ch has completed.
   */
  def pvGetComplete = ???

  /*
    void pvGetCancel(channel ch)

    Cancel a pending (asynchronous) pvGet.
   */
  def pvGetCancel = ???

  /*
    unsigned pvChannelCount()

    Returns the total number of process variables associated with the program.
   */
  def pvChannelCount = ???

  /*

 */

}

object Var {
  def apply[T: Format](init: T): Var[T]                                                    = new Var(init)
  def assign[T: Format](init: T, key: String)(implicit machine: Machine[_]): ProcessVar[T] = new ProcessVar[T](init, key)
}
