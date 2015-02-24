package euchre

import scala.io.StdIn

/**
 * Created by kyle on 2/23/15.
 */
class GameMaster(private var _state: String) {

  //    Valid states:
  //     Initial
  //     Dealt
  //     Play
  //     Round Complete
  def state = _state
  def state_=(s:String): Unit = (_state = s)

  var deck = new Deck()
  var gameArea = new GameArea(scoreboard)
  var p1 = new Player(new Hand(), new Schema())
  var p2 = new Player(new Hand(), new Schema())
  var p3 = new Player(new Hand(), new Schema())
  var p4 = new Player(new Hand(), new Schema())
  var playerOrder = new PlayerOrder(p1, p3, p2, p4)
  var t1 = new Team(p1, p2)
  var t2 = new Team(p3, p4)
  var scoreboard = new Scoreboard(playerOrder, t1, t2)
  changeState

  def changeState():Unit = {
    state match {
      case "Initial" => {
        println("Starting a new game...")
        val name = StdIn.readLine("What is your name? ")
        println("Thanks for playing Euchre, " + name + ". Setting up your game now.")
        // Deck
        deck.init
        // Game Area
        // Players and Hand
        for (p <- playerOrder.players) {
          p.init
          p.hand.init
        }
        p1.name = name
        // Round
        // Scoreboard
        scoreboard.init
        // Team
        t1.init
        t2.init
        println(" Your game of Euchre is set up and ready to go! Here are the details:")
        println(" Your team: " + t1.toString())
        println(" The other team: " + t2.toString())
        println(" The playing order is: " + playerOrder.toString())
        println("Now we are ready to play!")
        _state = "Deal"
        changeState()
      }
      case "Deal" => {
        println("Dealing cards...")
        _state = "Play"
        changeState()
      }
      case "Play" => {
        println("Playing...")
        // TODO:// Deal cards
        // TODO:// Set up game area
        // TODO:// Set up dealer
        // TODO:// Start round
        // TODO:// Play tricks
        // TODO:// Win tricks
        // TODO:// Win round
      }
      case "Round Complete" =>
        _state = "Quit"
        changeState()
      case "Quit" => println("Thanks for playing!")
      case default => {
        _state = "Initial"
        changeState()
      }
    }
  }
}
