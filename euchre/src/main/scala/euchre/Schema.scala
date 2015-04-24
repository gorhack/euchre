package euchre

import scala.util.Random

/**
 * Created by kyle on 2/11/15.
 */
class Schema(private var _schema: String) {
// Schemas:
//  1. Aggressive: Always play highest card in hand if possible to win trick
//  2. Passive: Play lowest card to allow teammate to win tricks - play highest cards last
//  3. Semi-Aggressive: Always play highest card in hand unless partner is winning the trick
//  4. Aggressive-Lead: When partner leads, always play highest card if possible to increase
//     chance of taking trick. Always lead with highest cards.
//  5. Passive-Lead: Always lead with low cards to allow partner to take tricks.
//  6. Aggressive-Trump: Always trump to take trick when possible
//  7. Passive-Fail: Always fail off to allow teammate to take trick.

// Alternate easier schema:
// 1. (COMPLETE) Aggressive: Play highest card available. Trump with highest trump when available.
// 2. Semi-Aggressive: Play highest card available. Trump with lowest trump when available.
// 3. Passive: Play lowest card available. Trump with lowest trump when available.
// 4. Aggressive-Trump: Play lowest card available. Trump with highest trump when available.
// 5. Aggressive-Fail: Play highest card available. Do not trump until the end.
// 6. Passive-Fail: Play lowest card available. Do not trump until the end.

  def schemas = List("Aggressive", "Passive ", "Semi-Aggressive",
                    "Aggressive-Lead", "Aggressive-Fail",
                    "Passive-Fail")
  def init = {
    // Randomly assign a schema
    _schema = Random.shuffle(schemas).head
  }
  def schema = _schema
  def schema_=(s:String): Unit = { _schema = s }

  override def toString() = _schema.mkString("")

  /*
   * Play card with schema
   */
  def playCard(_playSchema: String): Unit = {
    _playSchema match {
      case "Passive" => passiveSchema
      case "Semi-Aggressive" => semiAggressiveSchema
      case "Aggressive-Lead" => aggressiveLeadSchema
      case "Aggressive-Fail" => aggressiveFailSchema
      case "Passive-Fail" => passiveFailSchema
      case _ => aggressiveSchema
    }
  }
  // Aggressive: Play highest card available. Trump with highest trump when available.
  def aggressiveSchema: Unit = {
    println("Now using aggressive playing schema")
  }

  // Passive: Play lowest card available. Trump with lowest trump when available.
  def passiveSchema: Unit = {
    println("Now using passive playing schema")
    aggressiveSchema
  }

  // Semi-Aggressive: Play highest card available. Trump with lowest trump when available.
  def semiAggressiveSchema: Unit = {
    println("Now using semi-aggressive playing schema")
    aggressiveSchema
  }

  // Aggressive-Lead: Play highest card available. Do not trump until the end.
  def aggressiveLeadSchema: Unit = {
    println("Now using aggressive-lead playing schema")
    aggressiveSchema
  }

  // Aggressive-Trump: Play lowest card available. Trump with highest trump when available.
  def aggressiveFailSchema: Unit = {
    println("Now using aggressive-fail playing schema")
    aggressiveSchema
  }

  // Passive-Fail: Play lowest card available. Do not trump until the end.
  def passiveFailSchema: Unit = {
    println("Now using passive-fail playing schema")
    aggressiveSchema
  }

  /*
   * Schema helper functions
   */
/*
  // Determine the highest card in hand
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
*/
}
