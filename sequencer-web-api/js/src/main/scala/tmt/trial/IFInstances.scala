package tmt.trial

object IFInstances {
  implicit def any[T]: IF[T] = new IF[T] {
    override def toPb(x: T): PlatformExtensions.Items = ???
  }
}
