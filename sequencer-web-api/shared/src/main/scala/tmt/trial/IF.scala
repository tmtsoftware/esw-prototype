package tmt.trial

trait IF[T] {
  def toPb(x: T): PlatformExtensions.Items
}

object IF {
  def apply[T](implicit x: IF[T]): IF[T] = x
}
