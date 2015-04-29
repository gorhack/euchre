package euchre

/**
 * Created by kyle on 3/30/15.
 */
class Controller(private var view: View, private var model: Model) {
  def init: Unit = {
    model.init
  }

  def advancePlayerOrder(): Unit = {
    //println("Old Player Order " + model.playerOrder)
    model.advancePlayerOrder()
    //println("New Player Order " + model.playerOrder)
    view.setNextPlayer(model.indexOfCurrentPlayer)
    view.displayGameArea(scoreboard, round, playerOrder, trick)
  }

  def playCard(): Unit = {
    if (model.playCard()) {
      view.displayWinner(model.gameOver())
    }
    else {
      view.setNextPlayer(model.playerOrder.indexOfCurrentPlayer)
      view.displayPlayers()
      view.displayGameArea(scoreboard, round, playerOrder, trick)
    }
  }

  def round: Round = model.round

  def playRound(delay: Int): Boolean = {
    // do not play round if game is over
    if (scoreboard.highScore._2 >= 10) return true

    // local val for the number of tricks currently in the round
    val numTricks = model.round.tricks.length
    if (numTricks != 0 && model.round.tricks.last.cards.length != 4) {
      // in the middle of a round, make sure last trick is complete
      var currentTrick = model.round.tricks.last
      for (p <- currentTrick.cards.length until 4) {
        // there are 4 players
        playCard
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
        playCard
        Thread.sleep(delay)
      }
    }

    // check if end of game. return true if end of game, otherwise false
    if (scoreboard.highScore._2 >= 10) true
    else false
  }

  def playGame(delay: Int): Unit = {
    while (!playRound(delay)) {
    }
  }

  def playerOrder: PlayerOrder = {
    model.playerOrder
  }

  def schemas: List[String] = {
    model.schemas
  }

  def playerCards(_player: Int): String = {
    model.playerCards(_player)
  }

  def scoreboard: Scoreboard = {
    model.scoreboard
  }

  def roundScoreboard: String = {
    model.roundScoreboard
  }

  def trump: String = {
    model.trump
  }

  def playerName(_player: Int): String = {
    model.playerName(_player)
  }

  def trick: String = {
    model.trick
  }

  def setSchema(_player: Player, _schema: Schema): Unit = {
    model.setSchema(_player, _schema)
  }
}
