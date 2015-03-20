package euchre

import java.awt.Color

/**
 * Created by kyle on 2/11/15.
 */
class Card(val _displayValue: String, val _value: Int, val _suit: String, val _color: Color) {
  // displayValue, value, suit, color
  override def toString() = _displayValue + ' ' + _suit

  def displayValue: String = { _displayValue }
  def value: Int = { _value }
  def suit: String = { _suit }
  def color: Color = { _color}
}
