package euchre

import java.awt.Color

/**
 * Created by kyle on 2/11/15.
 *
 * Card class creates card object that contains:
 *   display value (8, 9, 10, J, Q, K, etc)
 *   value (8, 9, 10, 11, 12, 13, etc)
 *   suit (Diamond, Heart, Spade, Club)
 *   color (black, red)
 *   fileName
 */
class Card(val _displayValue: String, val _value: Int, val _suit: String, val _color: Color, val _fileName: String) {
  // displayValue, value, suit, color
  override def toString() = _displayValue + ' ' + _suit

  def displayValue: String = { _displayValue }
  def value: Int = { _value }
  def suit: String = { _suit }
  def color: Color = { _color}
  def fileName: String = { _fileName }
}
