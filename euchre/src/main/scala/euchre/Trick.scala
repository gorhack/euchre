package euchre

/**
 * Created by kyle on 2/11/15.
 */
class Trick {
  private var _cards: List[Card] = List.empty
  private var _leader: Player = null
  private var _isTrumped = false
  def isTrumped_=(t:Boolean): Unit = { _isTrumped = t }
  private var _winner: Player = null
  private var _winningTeam: Team = null
  def leader: Player = _leader
  def cards: List[Card] = _cards
  def cards_(c:List[Card]) = (_cards = c)
  def isTrumped: Boolean = _isTrumped
  def winner: Player = _winner
  def winningTeam: Team = _winningTeam

  def init = {
    _cards = List.empty
    _leader = null
    _isTrumped = false
    _winner = null
    _winningTeam = null
  }
  override def toString() = "Trick: (" + _cards.mkString(", ") + ")"
}
