/**
 * Created by kyle on 2/10/15.
 */
package euchre

import java.awt.Color

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

  /*
   * Getters and Setters
   */
  def name: String = _name
  def name_=(n:String): Unit = { _name = n }
  def hand = _hand
  def hand_(h:Hand) = { _hand = h }
  def schema: Schema = _schema
  def schema_(s:Schema) = (_schema = s)
  def isLead: Boolean = _isLead
  def isLead_(l:Boolean) = (_isLead = l)
  def canPlayCard: Boolean = _canPlayCard
  def canPlayCard_(p:Boolean) = (_canPlayCard = p)
  def playCard(lead: Boolean, trick: Trick, round: Round): Card = {
    // play card based on schema and return the card to be played
    _schema.playCard(this, _schema.toString(), lead, trick, round)
  }
  override def toString() = _name
}
