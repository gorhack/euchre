package euchre

/**
 * Created by kyle on 2/11/15.
 */
class Card(val _value: Char, val _suit: String) {
//  def value: Char = ???
//  def suit: String = ??? // suit class
  override def toString() = _value + ',' + _suit
  def value: Char = { _value }
  def suit: String = { _suit }
}
