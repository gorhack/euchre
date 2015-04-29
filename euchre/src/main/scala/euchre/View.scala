package euchre

/**
 * Created by kyle on 3/30/15.
 */

abstract class View {
  var controller: Option[Controller] = None

  def init(controller: Controller)
  def displayPlayers()
  def setNextPlayer(_indexOfCurrentPlayer: Int)
  def displayGameArea(_scoreboard: Scoreboard, _round: Round, _playerOrder: PlayerOrder, _trick: String)
  def displayWinner(_gameOver: String) // winner of trick
}
