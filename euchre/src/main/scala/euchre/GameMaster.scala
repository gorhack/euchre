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
            playerOrder.players(0).isLead_(false)
            playerOrder.players(1).isLead_(true)

            playerOrder.setPlayerOrder
            println("New Player Order: " + playerOrder.players.deep.mkString(" "))
            println()
          }
          case "2" => {
            // Step through game (complete 1 player move)
            gameArea.playCard
          }
          case "3" => {
            // Step through round (complete 1 round)
            var s = gameArea.playCards
            gameArea.updateScoreboard(s)

            // reset necessary game parts
            deck.init
            // Hand
            for (p <- playerOrder.players) {
              p.hand.init
            }
            playerOrder.players(0).isLead_(false)
            playerOrder.players(1).isLead_(true)
            playerOrder.setPlayerOrder

            println()
            if (scoreboard.highScore._2 < 10) gameArea.deal
            else _state = "Game Complete"
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
          // Set trump for round
          gameArea.setTrump
          playerOrder.players(0).isLead_(true)
          println()
          // Play cards
          var score = gameArea.playCards
          // Display Scoreboard
          gameArea.updateScoreboard(score)

          // reset necessary game parts
          deck.init
          // Hand
          for (p <- playerOrder.players) {
            p.hand.init
          }
          playerOrder.players(0).isLead_(false)
          playerOrder.players(1).isLead_(true)
          playerOrder.setPlayerOrder

          println()
          if (scoreboard.highScore._2 < 10) gameArea.deal
        }
        _state = "Game Complete"
        changeState()
      }
      case "Game Complete" => {
        var winningTeam: String = ""
        if (scoreboard.highScore._1 == 0) {
          winningTeam = "Team 1"
        }
        else {
          winningTeam = "Team 2"
        }
        println("Congrats " + winningTeam + " for winning today's game.")
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
