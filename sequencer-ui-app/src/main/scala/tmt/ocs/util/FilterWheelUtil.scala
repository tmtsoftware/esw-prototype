package tmt.ocs.util
import csw.messages.commands.{CommandName, ControlCommand, Observe, Setup}
import csw.messages.params.generics.KeyType.IntKey
import csw.messages.params.models.{ObsId, Prefix}

object FilterWheelUtil {
  def createMoveCommand(commandType: String, movePosition: Int): ControlCommand = {
    val positionKey = IntKey.make("position")
    val prefix      = Prefix("test-prefix")
    val commandName = CommandName("move")
    val someObsId   = Some(ObsId("test-obsId"))

    commandType match {
      case "Setup" =>
        Setup(prefix, commandName, someObsId, Set(positionKey.set(movePosition)))
      case "Observe" =>
        Observe(prefix, commandName, someObsId, Set(positionKey.set(movePosition)))
    }
  }
}
