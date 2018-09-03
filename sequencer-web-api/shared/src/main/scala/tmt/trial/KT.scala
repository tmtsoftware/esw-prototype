package tmt.trial
import IFInstances._

class KT[T: IF] {
  def instance(): IF[T] = IF[T]
}

object KT {
  object IntKT extends KT[Int]
}
