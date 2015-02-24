/**
 * Created by kyle on 2/10/15.
 */
package euchre

import scala.util.Random

class Player(private var _hand: Hand, private var _schema: Schema) {
  private var _name = ""
  // random filler names
  private val names = List("Kyle", "John", "Katelyn", "David", "Kristen",
                          "Connor", "Helen", "Peter", "Paige")
  private var _isLead = false
  private var _canPlayCard = false
  def init = {
    _name = Random.shuffle(names).head
    _isLead = false
    _canPlayCard = false
    _schema.init
  }
  def name: String = _name
  def name_=(n:String): Unit = { _name = n }
  def teammate: Player = ???
  def hand = _hand
  def hand_(h:Hand) = { _hand = h }
  def schema: Schema = _schema
  def isLead: Boolean = _isLead
  def isLead_(l:Boolean) = (_isLead = l)
  def canPlayCard: Boolean = _canPlayCard
  def playCard(lead: String, suit: String): Card = {
    // TODO:// decide actual card to play based on schema, what was led, and trump
    var card = _hand.cards.head
    _hand.cards_=(_hand.cards.tail)
    card
  }
  override def toString() = _name
}
