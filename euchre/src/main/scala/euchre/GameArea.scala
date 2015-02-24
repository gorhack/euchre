/**
 * Created by kyle on 2/10/15.
 */
package euchre

class GameArea(var _scoreboard: Scoreboard) {
  private var _round = new Round()
  def round: Round = _round
  def updateScorebaord: Scoreboard = _scoreboard
  def displayScoreboard: Scoreboard = _scoreboard
  def startNewRound = _round.init
}
