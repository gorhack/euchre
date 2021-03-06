/**
 * Created by kyle on 2/10/15.
 */
package euchre

class Scoreboard(var _playerOrder: PlayerOrder, var t1: Team, var t2: Team) {
  private var _scores = (0,0)
  private var _highScore = (null, 0)
  def scores: (Int, Int) = _scores
  def scores_(s:(Int, Int)) = {_scores = s}
  def highScore: (Int, Int) = {
    // (team, score)
    if (_scores._1 > _scores._2) (0, _scores._1)
    else (1, _scores._2)
  }
  def init = {
    _scores = (0, 0)
    _highScore = (null, 0)
  }
  override def toString() = "The Score is " + scores._1 + " to " + scores._2
}
