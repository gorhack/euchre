/**
 * Created by kyle on 2/10/15.
 */
package euchre

class GameArea(private var _scoreboard: Scoreboard, private var _t1: Team,
               private var _t2: Team, private var _playerOrder: PlayerOrder,
                private var deck: Deck) {
  private var _round = new Round()
  def round: Round = _round
  def round_(r:Round):Unit = (_round = r)
  def updateScorebaord: Scoreboard = _scoreboard
  def displayScoreboard: Scoreboard = _scoreboard
  def scoreboard_(s:Scoreboard):Unit = (_scoreboard = s)
  def startNewRound = _round.init
  // set scoreboard
  def setScoreboard = {

  }
  // set dealer
  def setLead = {

  }
  // give players cards
  def deal = {
    for (p <- _playerOrder.players) {
      for (i <- 0 until 5) {
        p.hand_(new Hand(p.hand.cards :+ deck.deal))
      }
      println(p.name + "'s Hand: " + p.hand.sortHand)
    }
  }
  def setTrump = {
    val t = deck.showTopCard
    round.trump_(t.suit)
    round.color_(t.color)
    println("Trump for the round is " + round.trump + "s")
  }
  // play cards to trick
  def playCards = {
    for (t <- 0 until 5) {
      var trick = new Trick()
      for (p <- _playerOrder.players) {
        // TODO:// Follow rules...
        trick.cards_(trick.cards :+ p.playCard("",""))
      }
      println("Trick " + (t + 1) + ": " + trick)
      decideWinnerOfTrick(trick)
      round.tricks :+ trick
    }
  }
  // decide winner of tricks
  def decideWinnerOfTrick(trick: Trick) = {
    var highCard: Card = trick.cards.head
    val bowers = "J " + round.color.toString
    var leadSuit: String = highCard.suit
    for (c <- trick.cards) {
      if (c.displayValue + ' ' + c.color == bowers) {
        // card is either bower
        if (c.suit == round.trump) {
          // card is right bower
          highCard = c
        }
        else if (highCard.suit == round.trump && highCard.displayValue == 'J') {
          // card is left bower, high card is right bower
        }
        else {
          highCard = c
        }
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
        // card must follow suit
        if (c.value > highCard.value && c.suit == leadSuit && highCard.displayValue + ' ' + highCard.color != bowers) {
          // both card and 'high' card are not trump, card is higher
          highCard = c
        }
      }
    }

    val indexOfWinner = trick.cards.indexOf(highCard)
    val winningPlayer = _playerOrder.players(indexOfWinner)
    println("Winner: " + winningPlayer.toString() + " with a " + highCard.toString())
    for (p <- _playerOrder.players) {
      // no players have lead
      p.isLead_(false)
    }
    // set winning player to have lead
    winningPlayer.isLead_(true)
    if (winningPlayer == _t1.team(0) || winningPlayer == _t1.team(1)) {
      _t1.points_(_t1.points + 1)
    }
    else {
      _t2.points_(_t2.points + 1)
    }
    updateScorebaord
  }
  // update scoreboard
  def updateScoreboard = {
    _scoreboard.scores_(_t1.points, _t2.points)
    println(_scoreboard.toString())
  }
}
