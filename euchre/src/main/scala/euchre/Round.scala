/**
 * Created by kyle on 2/10/15.
 */
package euchre

class Round {
  private var _trump = ""
  private var _tricks: List[Trick] = List.empty
  private var _highScore:(Team, Int) = (null, 0)
  def teams: Array[Team] = ???
  def highScore: (Team, Int) = _highScore
  def tricks = _tricks
  def trump: String = _trump
  def trump_(t:String): Unit = (_trump = t)

  def init = {
    _trump = ""
    _tricks = List.empty
    _highScore = (null, 0)
  }
}
