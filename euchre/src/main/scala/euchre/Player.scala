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
  def name: String = _name
  def name_=(n:String): Unit = { _name = n }
  def teammate: Player = ???
  def hand = _hand
  def hand_(h:Hand) = { _hand = h }
  def schema: Schema = _schema
  def isLead: Boolean = _isLead
  def isLead_(l:Boolean) = (_isLead = l)
  def canPlayCard: Boolean = _canPlayCard
  def playCard(lead: Boolean, trick: Trick, round: Round): Card = {
    // TODO:// decide actual card to play based on schema

    // determine high card in hand
    def determineHighCardFollowSuit(suit: String): Card = {
      // go through hand, select card with highest value of suit
      var returnCard: Card = null
      val bowers = "J " + round.color.toString
      var hasTrump = false
      for (c <- hand.cards) {
        if (c.suit == round.trump || c.displayValue + ' ' + c.color == bowers) {
          hasTrump = true
        }
        if (c.suit == suit) {
          if (returnCard == null) {
            returnCard = c
          }
          if (returnCard != null) {
            if (c.displayValue == 'J') {
              // need to trump, have right bower
              returnCard = c
            }
            else if (c.value > returnCard.value && returnCard.displayValue + ' ' + returnCard.color != bowers) {
              // not trump, compare value
              returnCard = c
            }
          }
        }
        else if (c.color == round.color && c.displayValue == 'J' && round.trump == suit) {
          // need to trump, have left bower
          returnCard = c
        }
      }
      if (returnCard == null) {
        if (hasTrump) {
          for (c <- hand.cards) {
            if (c.displayValue + ' ' + c.color == bowers) {
              returnCard = c
            }
            if (c.suit == round.trump) {
              if (returnCard == null) {
                returnCard = c
              }
              else if (c.value < returnCard.value && returnCard.displayValue + ' ' + returnCard.color != bowers) {
                returnCard = c
              }
            }
          }
        }
        else {
          returnCard = hand.cards.head
          for (c <- hand.cards) {
            if (c.value < returnCard.value) {
              returnCard = c
            }
          }
        }
      }
      returnCard
    }
    def determineHighCardLead: Card = {
      var highCard: Card = hand.cards.head
      val bowers = "J " + round.color.toString
      for (c <- hand.cards) {
        if (c.displayValue + ' ' + c.color == bowers) {
          // card is either bower
          highCard = c
        }
        else if (highCard.suit == round.trump && highCard.displayValue + ' ' + highCard.color != bowers) {
          if (c.suit == round.trump) {
            if (c.value > highCard.value) {
              // if card is trump and higher than the current 'high' trump card
              highCard = c
            }
          }
        }
        else if (c.suit == round.trump && highCard.displayValue + ' ' + highCard.color != bowers) {
          // card is trump and current 'high' card is not
          highCard = c
        }
        else {
          // card is not trump and current 'high' card is also not trump
          if (c.value > highCard.value && highCard.displayValue + ' ' + highCard.color != bowers) {
            // both card and 'high' card are not trump, card is higher
            highCard = c
          }
        }
      }
      highCard
    }

    var playingCard: Card = hand.cards.head
    // leading player, play highest card
    if (lead) {
      playingCard = determineHighCardLead
    }
    else {
      if (trick.cards.head.displayValue == 'J' && trick.cards.head.color == round.color) {
        playingCard = determineHighCardFollowSuit(round.trump)
      }
      else {
        playingCard = determineHighCardFollowSuit(trick.cards.head.suit)
      }
    }
    _hand.cards_=(_hand.cards.filter(_ != playingCard))
    playingCard
  }
  override def toString() = _name
}
