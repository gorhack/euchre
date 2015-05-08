package euchre

/**
 * Created by kyle on 3/30/15.
 */
class Controller(private var view: View, private var model: Model) {
  def init: Unit = {
    model.init
  }

  // player info
  def playerName(_player: Int): String = model.playerName(_player)
  def playerCards(_player: Int): String = model.playerCards(_player)
  def schemas: List[String] = model.schemas
  def setSchema(_player: Player, _schema: Schema): Unit = {
    model.setSchema(_player, _schema)
    view.displayPlayers()
  }

  // player order
  def playerOrder: PlayerOrder = model.playerOrder
  def advancePlayerOrder(): Unit = {
    // Advance the player order and update view
    model.advancePlayerOrder()
    view.setNextPlayer(model.indexOfCurrentPlayer)
    view.displayGameArea(scoreboard, round, playerOrder, trick)
  }

  // game info
  def scoreboard: Scoreboard = model.scoreboard
  def round: Round = model.round
  def roundScoreboard: String = "The score for this round is " + round.roundScore._1 + " to " + round.roundScore._2
  def trump: String = "Trump for the round is " + round.trump + "s" // return the trump as a string
  def trick: String = {
    // return the tricks as a string
    val tricks = round.tricks
    if (tricks.size != 0 && tricks.last.cards.size != 0) {
      " " + tricks.last.toString()
    }
    else if (tricks.size > 1 && tricks.last.cards.size == 0) {
      " " + tricks(tricks.length-2).toString()
    }
    else "ly no new tricks."
  }

  /*
   * Play Card
   */
  def playCard(): Boolean = {
    // if the game is over, update view to display winner
    var gameOver = model.playCard()
    if (gameOver) {
      view.displayWinner(model.gameOver())
    }
    // if the game is not over, update the view with played card
    else {
      view.setNextPlayer(model.playerOrder.indexOfCurrentPlayer)
      view.displayGameArea(scoreboard, round, playerOrder, trick)
      view.displayPlayers()
    }
    gameOver
  }

  /*
   * Play Round
   */
  def playRound(delay: Int): Boolean = {
    var gameOver = false

    // local val for the number of tricks currently in the round
    val numTricks = round.tricks.length
    if (numTricks != 0 && round.tricks.last.cards.length != 4) {
      // in the middle of a round, make sure last trick is complete
      var currentTrick = round.tricks.last
      for (p <- currentTrick.cards.length until 4) {
        // there are 4 players
        gameOver = playCard()
        Thread.sleep(delay)
      }

      // check if end of game. return true if end of game, otherwise false
      if (scoreboard.highScore._2 >= 10) true
      else false
    }

    // play a total of 5 tricks in the round
    for (t <- numTricks until 5) {
      // there are 5 tricks
      for (p <- 0 until 4) {
        // there are 4 players
        gameOver = playCard()
        Thread.sleep(delay)
      }
    }
    gameOver
  }

  /*
   * Play Full Game
   */
  def playGame(delay: Int): Unit = {
    while (!playRound(delay)) {
    }
  }
}
