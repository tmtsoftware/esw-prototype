package ocs.framework.dsl.epic.internal

import akka.stream.KillSwitch
import akka.stream.scaladsl.Sink

import scala.concurrent.Future
import scala.language.implicitConversions

class Var[T](init: T) {
  @volatile
  private var _value = init
  def set(x: T): Unit = {
    _value = x
  }

  def :=(x: T): Unit = set(x)
  def get: T         = _value

  override def toString: String = _value.toString
}

class ProcessVar[T](init: T, key: String)(implicit mc: Machine[_]) extends Var[T](init) {

  import mc.{ec, eventService, mat}

  def pvPut(): Future[Unit] = Future.unit.flatMap { _ =>
    eventService.publish(key, MockEvent(key, get))
  }

  def pvGet(): Future[Unit] = {
    eventService.get(key).map { option =>
      option.foreach { event =>
        set(event.value.asInstanceOf[T])
//        mc.refresh()
      }
    }
  }

  def monitor(): KillSwitch = {
    eventService
      .subscribe(key)
      .mapAsync(1) { event =>
        Future {
          set(event.value.asInstanceOf[T])
//          mc.refresh()
        }
      }
      .to(Sink.ignore)
      .run()
  }

  /*
    pvStat pvAssign(channel ch, char *pv_name)

    Assigns or re-assigns ch to a process variable with name pv_name. If pv_name is an empty string or NULL,
    then ch is de-assigned (not associated with any process variable)

    Note that pvAsssign is asynchronous: it sends a request to search for and connect to the given pv_name,
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
  def apply[T](init: T): Var[T]                                                    = new Var(init)
  def assign[T](init: T, key: String)(implicit machine: Machine[_]): ProcessVar[T] = new ProcessVar[T](init, key)
}
