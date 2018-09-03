package tmt.trial

object IFInstances {
  implicit object IntIF extends IF[Int] {
    override def toPb(x: Int): PlatformExtensions.Items = (1 to x).toList
  }
}
