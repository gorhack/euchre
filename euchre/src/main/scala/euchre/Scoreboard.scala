/**
 * Created by kyle on 2/10/15.
 */
package euchre

class Scoreboard(var _playerOrder: PlayerOrder, var t1: Team, var t2: Team) {
  private var _scores = (0,0)
  private var _highScore = (null, 0)
  def scores: (Int, Int) = _scores
  def highScore: (Team, Int) = _highScore
  def init = {
    _scores = (0, 0)
    _highScore = (null, 0)
  }
}
