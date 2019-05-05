package ui.animations

import typings.svgDotJsLib.svgDotJsMod.{Element, Line, Path, ^ => SVG}
import org.scalajs.dom.ext.Castable

object Diagram {
  val Mirror1: Line = SVG.get("mirror-1").cast[Line]
  val Mirror2: Line = SVG.get("mirror-2").cast[Line]
  val Mirror3: Line = SVG.get("mirror-3").cast[Line]

  val Light: Path = SVG.get("light").cast[Path]

  val Shutter1: Element    = SVG.get("shutter-1")
  val TopShutter1: Path    = SVG.get("top-shutter-1").cast[Path]
  val BottomShutter1: Path = SVG.get("bottom-shutter-1").cast[Path]

  val Shutter2: Element    = SVG.get("shutter-2")
  val TopShutter2: Path    = SVG.get("top-shutter-2").cast[Path]
  val BottomShutter2: Path = SVG.get("bottom-shutter-2").cast[Path]

  val Irms: Element = SVG.get("irms")
  val Iris: Element = SVG.get("iris")
  val Wfs: Element  = SVG.get("wfs")
}
