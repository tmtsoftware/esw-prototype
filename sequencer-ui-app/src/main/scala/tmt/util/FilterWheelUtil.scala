package tmt.util
import csw.messages.commands.{CommandName, ControlCommand, Observe, Setup}
import csw.messages.params.generics.KeyType.IntKey
import csw.messages.params.models.{ObsId, Prefix}

object FilterWheelUtil {
  def createMoveCommand(commandType: String, movePosition: Int): ControlCommand = {
    val positionKey = IntKey.make("position")
    commandType match {
      case "Setup" =>
        Setup(Prefix("test-prefix"), CommandName("move"), Some(ObsId("test-obsId")), Set(positionKey.set(movePosition)))
      case "Observe" =>
        Observe(Prefix("test-prefix"), CommandName("move"), Some(ObsId("test-obsId")), Set(positionKey.set(movePosition)))
    }
  }
}
