package org.tmt.macros

import scala.concurrent.ExecutionContext
import scala.reflect.macros.blackbox

object AsyncMacros {
  def async[T: c.WeakTypeTag](c: blackbox.Context)(body: c.Expr[T])(ec: c.Expr[ExecutionContext]) = {
    import c.universe._
    q"_root_.tmt.sequencer.dsl.Async.async($body)($ec)"
  }

  def asyncStrand[T: c.WeakTypeTag](c: blackbox.Context)(body: c.Expr[T])(standEc: c.Expr[StrandEc]): c.Tree = {
    import c.universe._
    val ec = reify(standEc.splice.ec)
    q"_root_.tmt.sequencer.dsl.Async.async($body)($ec)"
  }

  def await(c: blackbox.Context) = {
    import c.universe._
    val arg = c.prefix.tree.asInstanceOf[Apply].args.head
    q"_root_.tmt.sequencer.dsl.Async.await($arg)"
  }
}
