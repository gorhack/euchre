package euchre

/**
 * Created by kyle on 3/30/15.
 */
class Model {
  var _deck = new Deck()

  private var _p1 = new Player(new Hand(List.empty), new Schema(""))
  private var _p2 = new Player(new Hand(List.empty), new Schema(""))
  private var _p3 = new Player(new Hand(List.empty), new Schema(""))
  private var _p4 = new Player(new Hand(List.empty), new Schema(""))
  private var _playerOrder = new PlayerOrder(_p1, _p3, _p2, _p4)
  private var _t1 = new Team(_p1, _p2)
  private var _t2 = new Team(_p3, _p4)
  private var _scoreboard = new Scoreboard(_playerOrder, _t1, _t2)
  private var _gameArea = new GameArea(_scoreboard, _t1, _t2, _playerOrder, _deck)

  private var _schema = new Schema("")

  def init: Unit = {
    _deck.init
    // Game Area
    // Players and Hand
    for (p <- _playerOrder.players) {
      p.init
      p.hand.init
    }
    _p1.isLead_(true)
    _playerOrder.setPlayerOrder
    _playerOrder.indexOfCurrentPlayer_(0)
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

  def scoreboard: Scoreboard = _scoreboard
  def round: Round = _gameArea.round
  def playerOrder: PlayerOrder = _playerOrder
  def playerOrder_(p: PlayerOrder): Unit = (_playerOrder = p)
  def indexOfCurrentPlayer = _gameArea.indexOfCurrentPlayer

  def advancePlayerOrder(): Unit = {
    _gameArea.advancePlayerOrder()
    playerOrder_(_gameArea.playerOrder)
  }

  def playCard(): Boolean = {
    _gameArea.playCard
  }

  // set up schema
  def schemas: List[String] = {
    _schema.schemas
  }

  def playerCards(_player: Int): String = {
    _player match {
      case 0 => _p1.hand.toString()
      case 1 => _p2.hand.toString()
      case 2 => _p3.hand.toString()
      case 3 => _p4.hand.toString()
      case default => "Error retrieving player card: " + _player
    }
  }

  def roundScoreboard: String = {
    "The score for this round is " + _gameArea.round.roundScore._1 + " to " + _gameArea.round.roundScore._2
  }

  def trick: String = {
    val tricks = _gameArea.round.tricks
    if (tricks.size != 0 && tricks.last.cards.size != 0) {
      " " + tricks.last.toString()
    }
    else if (tricks.size > 1 && tricks.last.cards.size == 0) {
      " " + tricks(tricks.length-2).toString()
    }
    else "ly no new tricks."
  }

  def trump: String = {
    "Trump for the round is " + _gameArea.round.trump + "s"
  }

  def playerName(_player: Int): String = {
    _player match {
      case 0 => _p1.name
      case 1 => _p2.name
      case 2 => _p3.name
      case 3 => _p4.name
      case default => "Error retrieving player name: " + _player
    }
  }

  def gameOver(): String = {
    if (scoreboard.highScore._1 == 0) {
      "<html>Congrats, you win!" +
        "<br><br>The Score is " + _scoreboard.toString() + "</html>"
    }
    else {
      "<html>Sorry, you lose. Team 2 won." +
        "<br><br>The Score is " + _scoreboard.toString() + "</html>"
    }
  }

  def setSchema(_player: Player, _schema: Schema): Unit = {
    println(_player.toString() + "'s playing schema changed to " + _schema.toString())
    _player.schema.schema_=(_schema.toString())
  }
}
