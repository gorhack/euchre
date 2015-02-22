package euchre

/**
 * Created by kyle on 2/11/15.
 */
class Card(val value: Char, val suit: String) {
//  def value: Char = ???
//  def suit: String = ??? // suit class
  override def toString() = value + ',' + suit
  def Val: Char = { value }
  def suitOf: String = { suit }
}
