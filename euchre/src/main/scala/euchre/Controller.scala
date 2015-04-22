package euchre

/**
 * Created by kyle on 3/30/15.
 */
class Controller(private var view: View, private var model: Model) {
  def init: Unit = {
    model.init
  }

  def advancePlayerOrder(): Unit = {
    println("Advance Player Order")
    model.advancePlayerOrder()
  }

  def playCard(): Unit = {
    println("Play card")
    model.playCard()
  }

  def playRound(): Unit = {
    println("Play Round")
    model.playRound()
  }

  def playGame(): Unit = {
    println("Play Game")
    model.playGame()
  }

  def playerOrder: Array[Player] = {
    model.playerOrder
  }

  def schemas: List[String] = {
    model.schemas
  }

  def playerCards: Array[Hand] = {
    model.playerCards
  }

  def scoreboard: Scoreboard = {
    model.scoreboard
  }

  def trick: Trick = {
    model.trick
  }
}
