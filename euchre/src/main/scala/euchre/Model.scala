package euchre

/**
 * Created by kyle on 3/30/15.
 */
class Model {
  var _deck = new Deck()

  var _p1 = new Player(new Hand(List.empty), new Schema())
  var _p2 = new Player(new Hand(List.empty), new Schema())
  var _p3 = new Player(new Hand(List.empty), new Schema())
  var _p4 = new Player(new Hand(List.empty), new Schema())
  var _playerOrder = new PlayerOrder(_p1, _p3, _p2, _p4)
  var _t1 = new Team(_p1, _p2)
  var _t2 = new Team(_p3, _p4)
  var _scoreboard = new Scoreboard(_playerOrder, _t1, _t2)
  var _gameArea = new GameArea(_scoreboard, _t1, _t2, _playerOrder, _deck)

  var _schema = new Schema

  def init: Unit = {
    _deck.init
    // Game Area
    // Players and Hand
    for (p <- _playerOrder.players) {
      p.init
      p.hand.init
    }
    _p1.isLead_(true)
    // Scoreboard
    _scoreboard.init
    // Team
    _t1.init
    _t2.init
    _gameArea.startNewRound

    _gameArea.deal
    _gameArea.setTrump

    println(" Your game of Euchre is set up and ready to go! Here are the details:")
    println(" Your team: " + _t1.toString())
    println(" The other team: " + _t2.toString())
    println(" The playing order is: " + _playerOrder.toString())
    println("Now we are ready to play!")
  }

  def advancePlayerOrder(): Unit = {
    _gameArea.advancePlayerOrder()
    println("New Player Order: " + _playerOrder.players.deep.mkString(" "))
  }

  def playCard(): Unit = {
    _gameArea.playCard
    if (_scoreboard.highScore._2 >= 10) {
      gameOver()
    }
  }

  def playRound(): Unit = {
    _gameArea.playCards
    if (_scoreboard.highScore._2 >= 10) {
      gameOver()
    }
  }

  def playGame(): Unit = {
    while (_scoreboard.highScore._2 < 10) {
      // Play cards
      _gameArea.playCards
    }
    gameOver()
  }

  def playerOrder: Array[Player] = {
    _playerOrder.players
  }
  // set up schema
  def schemas: List[String] = {
    _schema.schemas
  }

  def gameOver(): Unit = {
    println("Game Over")
  }
}
