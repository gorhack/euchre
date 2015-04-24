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
