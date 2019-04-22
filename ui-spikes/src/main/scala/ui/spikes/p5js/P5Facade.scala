package ui.spikes.p5js

import typings.p5Lib.p5Mod.{namespaced => P5}
import typings.p5Lib.p5Mod.p5Ns.p5InstanceExtensions

import scala.scalajs.js

object P5Facade {

  /**
   * We need this because the function `sketch` provided to `P5` has not been specified well in typescript
   *
   * @param sketch a closure that can set optional preload(), setup(), and/or draw() properties on the given p5 instance
   */
  def apply(sketch: js.Function1[P5Config with p5InstanceExtensions, Unit]): P5 =
    new P5(sketch.asInstanceOf[js.Function1[js.Any, _]])

  /**
   * We need this because in the `p5` trait, the functions have been translated to methods, so we can't change them
   * */
  @js.native
  trait P5Config extends js.Object {
    var draw: js.Function0[Unit]    = js.native
    var preload: js.Function0[Unit] = js.native
    var remove: js.Function0[Unit]  = js.native
    var setup: js.Function0[Unit]   = js.native
  }
}
