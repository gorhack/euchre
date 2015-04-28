package euchre

/**
 * Created by kyle on 3/30/15.
 */

abstract class View {
  var controller: Option[Controller] = None

  def init(controller: Controller)
  def displayPlayerOrder(_playerOrder: PlayerOrder)
  def displayCurrentPlayer(_player: Player)
  def displayTrick(_trick: Trick)
  def displayWinner(_player: Player) // winner of trick
}
