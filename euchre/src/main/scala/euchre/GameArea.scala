/**
 * Created by kyle on 2/10/15.
 */
package euchre

class GameArea(private var _scoreboard: Scoreboard, private var _t1: Team,
               private var _t2: Team, private var _pOrder: PlayerOrder,
                private var deck: Deck) {
  /*
   * Sets the defaults and makes getters and setters
   */
  private var _game = Array.empty[(Round, Int)]
  def game = _game
  def game_(g: Array[(Round, Int)]): Unit = (_game = g)
  private var _round = new Round()
  def round: Round = _round
  def round_(r:Round): Unit = (_round = r)
  def updateScoreboard: Scoreboard = _scoreboard
  def displayScoreboard: Scoreboard = _scoreboard
  def scoreboard_(s:Scoreboard):Unit = (_scoreboard = s)
  def startNewRound = _round.init
  private var _playerOrder = _pOrder
  def playerOrder = _playerOrder
  def playerOrder_(p: PlayerOrder): Unit = (_playerOrder = p)

  /*
   * Deal cards to players
   */
  def deal = {
    for (p <- playerOrder.players) {
      for (i <- 0 until 5) {
        // players each have 5 cards in hand
        p.hand_(new Hand(p.hand.cards :+ deck.deal))
      }
      p.hand.sortHand
      // print the hand to the console
      println(p.name + "'s " + p.hand.toString())
    }
  }

  /*
   * Sets the trump for the round
   */
  def setTrump = {
    val t = deck.showTopCard
    round.trump_(t.suit)
    round.color_(t.color)
    println("Trump for the round is " + round.trump + "s")
  }

  /*
   * The next player in the player order plays a card
   */
  def playCard: Boolean = {
    // do not play card if game is over
    if (_scoreboard.highScore._2 >= 10) return true

    if (_round.tricks.length == 0) {
      // if new round add a trick
      _round.tricks_(_round.tricks :+ new Trick())
    }

    // local values for the current truck and number of cards in the trick
    val currentTrick = _round.tricks.last
    val numCards = currentTrick.cards.length
    // local value for the current player up in the player order
    // based on the number of cards played in the current trick
    val currentPlayer = playerOrder.players(playerOrder.indexOfCurrentPlayer)
    playerOrder.indexOfCurrentPlayer_((playerOrder.indexOfCurrentPlayer + 1) % 4)

    // have the current player play a card
    currentTrick.cards_(currentTrick.cards :+ currentPlayer.playCard(currentPlayer.isLead,currentTrick,_round))

    println(currentTrick.toString())
    if (numCards == 3) {
      // if the last player has played, decide winner and update round scoreboard
      val winningTeam = decideWinnerOfTrick(currentTrick)
      updateRoundScoreboard
      if (_round.tricks.length == 5) {
        // if all 5 tricks have been played update scoreboard and reset for new round
        updateScoreboard(_round.roundScore)
        game_(game :+ (_round, winningTeam))
        startNewRound
        deck.init
        deal
        setTrump
        // reset player order, advance player order by 1
        for (p <- playerOrder.players) {
          p.isLead_(false)
        }

        // after each round of Euchre the player to the right of the player that started the round leads into the next round
        playerOrder.players(game.length % 4).isLead_(true)
        playerOrder.indexOfCurrentPlayer_(game.length % 4)
        playerOrder.players(playerOrder.indexOfCurrentPlayer).isLead_(true)
      }
      else {
        // if not all 5 tricks have been played start a new trick
        _round.tricks_(_round.tricks :+ new Trick())
      }
    }

    // check if end of game. return true if end of game, otherwise false
    if (_scoreboard.highScore._2 >= 10) true
    else false
  }

  /*
   * Determine the high card in a trick
   */
  def determineHighCard(trick: Trick): Card = {
    // set default high card to the first card
    var highCard: Card = trick.cards.head
    // establish the bowers (highest trump in game)
    val bowers = "J " + round.color.toString
    // the lead suit is the suit of the first card played, which is currently the high card
    var leadSuit: String = highCard.suit
    for (c <- trick.cards) {
      /*
       * First case: Current card is one of the bowers
       */
      if (c.displayValue + ' ' + c.color == bowers) {
        if (c.suit == round.trump) {
          // card is right bower
          return c
        }
        else if (highCard.suit == round.trump && highCard.displayValue == 'J') {
          // card is left bower, high card is right bower
        }
        else {
          return c
        }
      }

      /*
       * Second case: Both current card and high card are trump, but not a bowers
       */
      else if (highCard.suit == round.trump) {
        if (c.suit == round.trump) {
          if (c.value > highCard.value) {
            // if card is trump and higher than the current 'high' trump card
            highCard = c
          }
        }
      }

      /*
       * Third case: Current card is trump and the high card is not trump
       */
      else if (c.suit == round.trump) {
        // card is trump and current 'high' card is not
        highCard = c
      }
      else {
        // card is not trump and current 'high' card is also not trump
        // card must follow suit
        if (c.value > highCard.value && c.suit == leadSuit) {
          // both card and 'high' card are not trump, card is higher
          highCard = c
        }
      }
    }
    highCard
  }

  /*
   * Determine the winner of a trick
   */
  def decideWinnerOfTrick(trick: Trick): Int = {
    // local variables for high card, index of winner, and winning player
    val highCard = determineHighCard(trick)
    val indexOfWinner = trick.cards.indexOf(highCard)
    val winningPlayer = _playerOrder.players((playerOrder.indexOfCurrentPlayer + indexOfWinner) % 4)
    println("Winner: " + winningPlayer.toString() + " with a " + highCard.toString())

    // reset the leader
    for (p <- _playerOrder.players) {
      // no players have lead
      p.isLead_(false)
    }
    // set winning player to have lead
    winningPlayer.isLead_(true)
    // set new player order
    playerOrder.indexOfCurrentPlayer_((playerOrder.indexOfCurrentPlayer + indexOfWinner) % 4)


    // update the round score with the correct winning team
    var team1Score = _round.roundScore._1
    var team2Score = _round.roundScore._2
    if (winningPlayer == _t1.team(0) || winningPlayer == _t1.team(1)) {
      team1Score+=1
      _round.roundScore_(team1Score, team2Score)
      0
    }
    else {
      team2Score+=1
      _round.roundScore_(team1Score, team2Score)
      1
    }
  }

  /*
   * Advance the player order
   */
  def advancePlayerOrder(): Unit = {
    // do not advance player order mid trick
    if (game.length == 0 || game.last._1.tricks.isEmpty) {

      playerOrder.indexOfCurrentPlayer_(playerOrder.indexOfCurrentPlayer + 1)
    }
    else {
      println("Cannot advance player order mid round.")
    }
  }

  /*
   * Update round scoreboard
   */
  def updateRoundScoreboard = {
    // win a point in the round by winning a trick
    println("Round score is " + _round.roundScore._1 + " to " + _round.roundScore._2)
  }

  /*
   * Update game scoreboard
   */
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
    println()
    println(_scoreboard.toString())
  }
}
