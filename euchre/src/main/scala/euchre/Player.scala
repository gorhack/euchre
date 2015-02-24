/**
 * Created by kyle on 2/10/15.
 */
package euchre

import scala.util.Random

class Player(private var _hand: Hand, private var _schema: Schema) {
  private var _name = ""
  // possible names:
  // Kyle, John, Katelyn, David, Zac, Kristen
  private var names = List("Kyle", "John", "Katelyn", "David", "Kristen")
  private var _isDealer = false
  private var _canPlayCard = false
  def init = {
    _name = Random.shuffle(names).head
    _isDealer = false
    _canPlayCard = false
    _schema.init
  }
  def name: String = _name
  def name_=(n:String): Unit = { _name = n }
  def teammate: Player = ???
  def hand = _hand
  def schema: Schema = _schema
  def position: PlayerOrder = ???
  def isDealer: Boolean = _isDealer
  def canPlayCard: Boolean = _canPlayCard
}
