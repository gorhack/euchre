package euchre

import scala.util.Random

/**
 * Created by kyle on 2/11/15.
 */
class Schema(private var _schema: String) {

// Schemas:
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
  def playCard(_player: Player, _playSchema: String, lead: Boolean, trick: Trick, round: Round): Card = {
    _playSchema match {
      case "Passive" => passiveSchema(_player, lead, trick, round)
      case "Semi-Aggressive" => semiAggressiveSchema(_player, lead, trick, round)
      case "Aggressive-Lead" => aggressiveLeadSchema(_player, lead, trick, round)
      case "Aggressive-Fail" => aggressiveFailSchema(_player, lead, trick, round)
      case "Passive-Fail" => passiveFailSchema(_player, lead, trick, round)
      case _ => aggressiveSchema(_player, lead, trick, round)
    }
  }
  // Aggressive: Play highest card available. Trump with highest trump when available.
  def aggressiveSchema(_player: Player, lead: Boolean, trick: Trick, round: Round): Card = {
    println("Playing with aggressive schema")

    var playingCard: Card = _player.hand.cards.head
    // leading player, play highest card
    if (lead) {
      playingCard = determineHighCardLead(_player, round)
    }
    else {
      if (trick.cards.head.displayValue == 'J' && trick.cards.head.color == round.color) {
        playingCard = determineHighCardFollowSuit(_player, round, round.trump)
      }
      else {
        playingCard = determineHighCardFollowSuit(_player, round, trick.cards.head.suit)
      }
    }
    _player.hand.cards_=(_player.hand.cards.filter(_ != playingCard))
    playingCard
  }

  // Passive: Play lowest card available. Trump with lowest trump when available.
  def passiveSchema(player: Player, lead: Boolean, trick: Trick, round: Round): Card = {
    println("Playing with passive schema")
    aggressiveSchema(player, lead, trick, round)
  }

  // Semi-Aggressive: Play highest card available. Trump with lowest trump when available.
  def semiAggressiveSchema(player: Player, lead: Boolean, trick: Trick, round: Round): Card = {
    println("Playing with semi-aggressive schema")
    aggressiveSchema(player, lead, trick, round)
  }

  // Aggressive-Lead: Play highest card available. Do not trump until the end.
  def aggressiveLeadSchema(player: Player, lead: Boolean, trick: Trick, round: Round): Card = {
    println("Playing with aggressive-lead schema")
    aggressiveSchema(player, lead, trick, round)
  }

  // Aggressive-Trump: Play lowest card available. Trump with highest trump when available.
  def aggressiveFailSchema(player: Player, lead: Boolean, trick: Trick, round: Round): Card = {
    println("Playing with aggressive-fail schema")
    aggressiveSchema(player, lead, trick, round)
  }

  // Passive-Fail: Play lowest card available. Do not trump until the end.
  def passiveFailSchema(player: Player, lead: Boolean, trick: Trick, round: Round): Card = {
    println("Playing with passive-fail schema")
    aggressiveSchema(player, lead, trick, round)
  }

  /*
   * Schema helper functions
   */

  // Determine the highest card in hand
  // determine high card in hand
  def determineHighCardFollowSuit(player: Player, round: Round, suit: String): Card = {
    // go through hand, select card with highest value of suit
    var returnCard: Card = null
    val bowers = "J " + round.color.toString
    var hasTrump = false
    for (c <- player.hand.cards) {
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
        for (c <- player.hand.cards) {
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
        returnCard = player.hand.cards.head
        for (c <- player.hand.cards) {
          if (c.value < returnCard.value) {
            returnCard = c
          }
        }
      }
    }
    returnCard
  }
  def determineHighCardLead(player: Player, round: Round): Card = {
    var highCard: Card = player.hand.cards.head
    val bowers = "J " + round.color.toString
    for (c <- player.hand.cards) {
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

}
