/**
 * Created by kyle on 2/10/15.
 */
package euchre

class Team(private var p1: Player, private var p2: Player) {
  private var _tricks = 0
  private var _points = 0
  private var _hasLead = if (p1.isLead | p2.isLead) true else false
  def team = List(p1, p2)
  def size: Int = team.length
  def points: Int = _points
  def points_(p:Int) = (_points = p)
  def hasLead = _hasLead
  def tricks: Int = _tricks

  def init = {
    _tricks = 0
    _points = 0
    _hasLead = false
  }
  override def toString():String = {
    return (
      p1.name +
      " and " + p2.name +
      " with " + points.toString + " point(s)")
  }
}
