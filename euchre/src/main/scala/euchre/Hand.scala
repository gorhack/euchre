/**
 * Created by kyle on 2/10/15.
 */
package euchre

class Hand(private var _cards: List[Card]) {
  def cards = _cards
  def cards_=(s:List[Card]): Unit = { _cards = s }
  def init = {
    _cards = List.empty
  }

  // sort hand by suit and value to display pretty
  def sortHand = { _cards.sortBy(r => (r.suit, r.value))}
  def length: Int = cards.length

  override def toString() = "Hand: (" + _cards.mkString(", ") + ")"
}
