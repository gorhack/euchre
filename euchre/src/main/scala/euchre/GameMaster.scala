package euchre

import scala.io.StdIn

/**
 * Created by kyle on 2/23/15.
 */
class GameMaster(private var _state: String) {

  //    Valid states:
  //     Initial
  //     Play
  //     Game Complete
  //     Quit
  def state = _state
  def state_=(s:String): Unit = (_state = s)

  var name = ""

  var deck = new Deck()

  var p1 = new Player(new Hand(List.empty), new Schema())
  var p2 = new Player(new Hand(List.empty), new Schema())
  var p3 = new Player(new Hand(List.empty), new Schema())
  var p4 = new Player(new Hand(List.empty), new Schema())
  var playerOrder = new PlayerOrder(p1, p3, p2, p4)
  var t1 = new Team(p1, p2)
  var t2 = new Team(p3, p4)
  var scoreboard = new Scoreboard(playerOrder, t1, t2)
  var gameArea = new GameArea(scoreboard, t1, t2, playerOrder, deck)

  changeState

  def changeState():Unit = {
    state match {
      case "Initial" => {
        println("Starting a new game...")
        if (name == "") {
          name = StdIn.readLine("What is your name? ")
        }
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
        p1.isLead_(true)
        // Scoreboard
        scoreboard.init
        // Team
        t1.init
        t2.init
        gameArea.startNewRound
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
        gameArea.deal
        _state = "Run"
        changeState()
      }
      case "Run" => {
        var command = StdIn.readLine(
          """What would you like to do? (Enter 1-4)
            | 1. Advance Player Order
            | 2. Step through game (complete 1 player move)
            | 3. Step through round (complete 1 round)
            | 4. Run full game
          """.stripMargin)
        command match {
          case "1" => {
            // Advance Player Order
            gameArea.advancePlayerOrder()

            println("New Player Order: " + playerOrder.players.deep.mkString(" "))
            println()
          }
          case "2" => {
            // Step through game (complete 1 player move)
            gameArea.playCard

            if (scoreboard.highScore._2 >= 10) {
              _state = "Game Complete"
              changeState()
            }
          }
          case "3" => {
            // Step through round (complete 1 round)
            gameArea.playCards

            if (scoreboard.highScore._2 >= 10) {
              _state = "Game Complete"
              changeState()
            }
          }
          case "4" => {
            // Run through full game
            _state = "Play"
          }
          case default => {
            // Invalid enter, running through full game
            _state = "Play"
          }
        }
        changeState()
      }
      case "Play" => {
        println("Playing...")
        // Deal cards
        while (scoreboard.highScore._2 < 10) {
          println()
          // Play cards
          gameArea.playCards

          println()
        }
        _state = "Game Complete"
        changeState()
      }
      case "Game Complete" => {
        var winMessage: String = ""
        if (scoreboard.highScore._1 == 0) {
          winMessage = "Congrats, you win!"
        }
        else {
          winMessage = "Sorry, you lose. Team 2 won."
        }
        println(winMessage)
        println()
        val playAgain = StdIn.readLine("Play Again? (Y/N)")
        _state = if (playAgain.equals("Y")) "Initial" else "Quit"
        changeState()
      }
      case "Quit" => {
        println("Thanks for playing!")
        System.exit(0)
      }
      case default => {
        _state = "Initial"
        changeState()
      }
    }
  }
}
