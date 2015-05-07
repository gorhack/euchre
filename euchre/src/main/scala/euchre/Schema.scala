package euchre

import scala.util.Random

/**
 * Created by kyle on 2/11/15.
 */
class Schema(private var _schema: String) {

// Schemas:
// 1. Aggressive: Play highest card available. Trump with highest trump when available.
// 2. Semi-Aggressive: Play highest card available. Trump with lowest trump when available.
// 3. Passive: Play lowest card available. Trump with lowest trump when available.
// 4. Aggressive-Trump: Play lowest card available. Trump with highest trump when available.
// 5. Aggressive-Fail: Play highest card available. Do not trump until the end.
// 6. Passive-Fail: Play lowest card available. Do not trump until the end.

  def schemas = List("Aggressive", "Passive ", "Semi-Aggressive",
                    "Aggressive-Trump", "Aggressive-Fail",
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
    var playingCard: Card = _player.hand.cards.head
    println("Playing with " + _playSchema + " schema")

    if (_playSchema == "Aggressive" || _playSchema == "Semi-Aggressive" || _playSchema == "Aggressive-Fail") {
      // leading player, play highest card
      if (lead) {
        playingCard = determineHighCardLead(_player, round)
      }
      else {
        // follow suit, play highest card. Will trump with highest card if able to.
        playingCard = determineHighCardFollowSuit(_player, round, trick.cards.head.suit)
      }
    }
    else {
      if (lead) {
        playingCard = determineLowCardLead(_player, round)
      }
      else {
        // follow suit, play highest card. Will trump with highest card if able to.
        playingCard = determineLowCardFollowSuit(_player, round, trick.cards.head.suit)
      }
    }

    // remove card from player's hand
    _player.hand.cards_=(_player.hand.cards.filter(_ != playingCard))
    playingCard
  }

  /*
   * Schema helper functions
   */

  // return highest card of suit, or return lowest card
  def determineHighCardFollowSuit(player: Player, round: Round, suit: String): Card = {
    // go through hand, select card with highest value of suit
    var returnCard: Card = null
    val bowers = "J " + round.color.toString
    var hasTrump = false
    for (c <- player.hand.cards) {
      // go through every card in hand
      if (c.suit == round.trump || c.displayValue + ' ' + c.color == bowers) {
        // check if have trump
        hasTrump = true
      }
      if (c.suit == suit) {
        // card can follow suit
        if (returnCard == null) {
          // set return card if can follow suit
          returnCard = c
        }
        if (returnCard != null) {
          if (c.value > returnCard.value && c.displayValue + ' ' + c.color != bowers) {
            // card is a higher value than current returnCard
            returnCard = c
          }
        }
      }
    }
    if (returnCard == null) {
      if (hasTrump && (player.schema.toString() == "Aggressive")) {
        // could not follow suit, high trump schema
        returnCard = playHighestTrumpCard(player, round)
      }
      else if (hasTrump && (player.schema.toString() == "Semi-Aggressive")) {
        // could not follow suit, low trump schema
        returnCard = playLowestTrumpCard(player, round)
      }
      else {
        // fail off with lowest card
        returnCard = playLowestFailCard(player, round)
      }
    }
    returnCard
  }
  // return lowest card of suit, or return lowest card
  def determineLowCardFollowSuit(player: Player, round: Round, suit: String): Card = {
    // go through hand, select card with lowest value of suit
    var returnCard: Card = null
    val bowers = "J " + round.color.toString
    var hasTrump = false
    for (c <- player.hand.cards) {
      // go through every card in hand
      if (c.suit == round.trump || c.displayValue + ' ' + c.color == bowers) {
        // check if have trump
        hasTrump = true
      }
      if (c.suit == suit) {
        // card can follow suit
        if (returnCard == null) {
          // set return card if can follow suit
          returnCard = c
        }
        if (returnCard != null) {
          if (c.value < returnCard.value && c.displayValue + ' ' + c.color != bowers) {
            // card is a lower value than current returnCard
            returnCard = c
          }
        }
      }
    }
    if (returnCard == null) {
      if (hasTrump && (player.schema.toString() == "Aggressive-Trump")) {
        // could not follow suit, high trump schema
        returnCard = playHighestTrumpCard(player, round)
      }
      else if (hasTrump && (player.schema.toString() == "Passive")) {
        returnCard = playLowestTrumpCard(player, round)
      }
      else {
        // fail off with lowest card
        returnCard = playLowestFailCard(player, round)
      }
    }
    returnCard
  }
  // returns highest card
  def determineHighCardLead(player: Player, round: Round): Card = {
    var highCard: Card = player.hand.cards.head
    val bowers = "J " + round.color.toString
    var hasBower = false
    for (c <- player.hand.cards) {
      if (c.displayValue + ' ' + c.color == bowers) {
        // card is either bower, skip
        hasBower = true
      }
      if (c.value > highCard.value && highCard.displayValue + ' ' + highCard.color != bowers) {
        // both card and 'high' card are not trump, card is higher
        highCard = c
      }
    }
    // all cards in hand are trump
    if (player.hand.cards.count(_.suit == round.trump) == 5  ||
       (player.hand.cards.count(_.suit == round.trump) == 4 && hasBower)) {
      highCard = allTrumpLead(player, round)
    }
    highCard
  }
  // returns lowest card
  def determineLowCardLead(player: Player, round: Round): Card = {
    var lowCard: Card = player.hand.cards.head
    val bowers = "J " + round.color.toString
    var hasBower = false
    for (c <- player.hand.cards) {
      if (c.displayValue + ' ' + c.color == bowers) {
        // card is either bower, skip
        hasBower = true
      }
      // card is not trump and current 'low' card is also not trump
      if (c.value < lowCard.value) {
        // both card and 'low' card are not trump, card is lower
        lowCard = c
      }
    }
    // all cards in hand are trump
    if (player.hand.cards.count(_.suit == round.trump) == 5  ||
      (player.hand.cards.count(_.suit == round.trump) == 4 && hasBower)) {
      lowCard = allTrumpLead(player, round)
    }
    lowCard
  }
  // return lowest non-trump card or first card
  def playLowestFailCard(player: Player, round: Round): Card = {
    var returnCard = player.hand.cards.head
    val noTrumpHand = player.hand.cards.filter(_.suit != round.trump)
    if (noTrumpHand.length == 0 ||
        (noTrumpHand.length == 1 && noTrumpHand.head.displayValue == 'J' && noTrumpHand.head.color == round.color)) {
      // only have trump left
      returnCard = playHighestTrumpCard(player, round)
    }
    for (c <- noTrumpHand) {
      if (c.value < returnCard.value) {
        returnCard = c
      }
    }
    returnCard
  }
  // return lowest trump card or first card
  def playLowestTrumpCard(player: Player, round: Round): Card = {
    var returnCard = player.hand.cards.head
    val trumpHand = player.hand.cards.filter(_.suit == round.trump)
    if (trumpHand.length == 0 && player.hand.cards.filter(_.displayValue == 'J').filter(_.color == round.color).length == 0) {
      // no trump in hand
      returnCard = playLowestFailCard(player, round)
    }
    for (c <- trumpHand) {
      if (c.value < returnCard.value) {
        returnCard = c
      }
    }
    returnCard
  }
  // return highest trump card or first card
  def playHighestTrumpCard(player: Player, round: Round): Card = {
    var returnCard = player.hand.cards.head
    val trumpHand = player.hand.cards.filter(_.suit == round.trump)
    if (trumpHand.length == 0 && player.hand.cards.filter(_.displayValue == 'J').filter(_.color == round.color).length == 0) {
      // no trump in hand
      returnCard = playLowestFailCard(player, round)
    }
    for (c <- trumpHand) {
      if (c.value > returnCard.value) {
        returnCard = c
      }
    }
    if (player.hand.cards.filter(_.displayValue == 'J').count(_.color == round.color) == 1) {
        // has bower
        returnCard = player.hand.cards.filter(_.displayValue == 'J').filter(_.color == round.color).head
    }
    returnCard
  }
  // play correct leading card when hand contains 5 trump
  def allTrumpLead(player: Player, round: Round): Card = {
    if (player.schema.toString() == "Aggressive" || player.schema.toString() == "Aggressive-Trump" || player.schema.toString() == "Aggressive-Fail") {
      playHighestTrumpCard(player, round)
    }
    else playLowestTrumpCard(player, round)
  }
}
