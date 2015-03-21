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
    // Player 1 is always the dealer
    _playerOrder.players(0).isLead_(true)
    _playerOrder.setPlayerOrder
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
  def playCards: (Int, Int) = {
    for (t <- 0 until 5) {
      // there are 5 tricks
      var trick = new Trick()
      for (p <- _playerOrder.players) {
        trick.cards_(trick.cards :+ p.playCard(p.isLead,trick,_round))
      }
      println("Trick " + (t + 1) + ": " + trick)
      decideWinnerOfTrick(trick)
      round.tricks :+ trick
    }
    _round.roundScore
  }

  // determine high card in trick
  def determineHighCard(trick: Trick): Card = {
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
    highCard
  }
  // determine winner of trick
  def decideWinnerOfTrick(trick: Trick) = {
    val highCard = determineHighCard(trick)
    val indexOfWinner = trick.cards.indexOf(highCard)
    val winningPlayer = _playerOrder.players(indexOfWinner)
    println("Winner: " + winningPlayer.toString() + " with a " + highCard.toString())
    for (p <- _playerOrder.players) {
      // no players have lead
      p.isLead_(false)
    }
    // set winning player to have lead
    winningPlayer.isLead_(true)
    // set new player order
    _playerOrder.setPlayerOrder
    if (winningPlayer == _t1.team(0) || winningPlayer == _t1.team(1)) {
      _round.roundScore_(_round.roundScore._1 + 1, _round.roundScore._2)
      //_t1.points_(_t1.points + 1)
    }
    else {
      _round.roundScore_(_round.roundScore._1, _round.roundScore._2 + 1)
      //_t2.points_(_t2.points + 1)
    }
    updateRoundScoreboard
  }
  // update round scoreboard
  def updateRoundScoreboard = {
    println("Round score is " + _round.roundScore._1 + " to " + _round.roundScore._2)
  }

  // update game scoreboard
  def updateScoreboard(score: (Int, Int)) = {
    var t1Points = _scoreboard.scores._1
    var t2Points = _scoreboard.scores._2

    if (score._1 == 0 && _t1.hasDeal) {
      // have deal and sweep
      t2Points += 2
    }
    else if (score._1 >= 3 && _t2.hasDeal) {
      // euchre
      t1Points += 2
    }
    else if (score._1 >= 3 && _t1.hasDeal) {
      // have lead and win
      t1Points += 1
    }
    else if (score._2 == 0 && _t2.hasDeal) {
      // have deal and sweep
      t1Points += 2
    }
    else if (score._2 >= 3 && _t1.hasDeal) {
      // euchre
      t2Points += 2
    }
    else if (score._2 >= 3 && _t2.hasDeal) {
      // have lead and win
      t2Points += 1
    }

    _scoreboard.scores_(t1Points, t2Points)
    // reset round scoreboard
    _round.roundScore_(0,0)
    println(_scoreboard.toString())
  }
}
