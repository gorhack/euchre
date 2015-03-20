/**
 * Created by kyle on 2/10/15.
 */
package euchre

import java.awt.Color

class Round {
  private var _trump = ""
  private var _color = Color.blue
  private var _tricks: List[Trick] = List.empty
  private var _highScore:(Team, Int) = (null, 0)
  private var _roundScore: (Int, Int) = (0, 0)
  def teams: Array[Team] = ???
  def highScore: (Team, Int) = _highScore
  def tricks = _tricks
  def trump: String = _trump
  def trump_(t:String): Unit = (_trump = t)
  def color: Color = _color
  def color_(c:Color): Unit = (_color = c)
  def roundScore: (Int, Int) = _roundScore
  def roundScore_(s:(Int, Int)): Unit = (_roundScore = s)

  def init = {
    _trump = ""
    _color = Color.blue
    _tricks = List.empty
    _highScore = (null, 0)
  }
}
