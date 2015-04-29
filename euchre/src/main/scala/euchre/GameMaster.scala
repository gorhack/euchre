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

  var p1 = new Player(new Hand(List.empty), new Schema(""))
  var p2 = new Player(new Hand(List.empty), new Schema(""))
  var p3 = new Player(new Hand(List.empty), new Schema(""))
  var p4 = new Player(new Hand(List.empty), new Schema(""))
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
        playerOrder.setPlayerOrder
        playerOrder.indexOfCurrentPlayer_(0)
        // Scoreboard
        scoreboard.init
        // Team
        t1.init
        t2.init
        gameArea.round.init
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
        gameArea.setTrump
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
            gameArea.playCard //TODO: does not use true false flag

            if (scoreboard.highScore._2 >= 10) {
              _state = "Game Complete"
              changeState()
            }
          }
          case "3" => {
            // Step through round (complete 1 round)


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
        while (!playRound(300)) {
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
        _state = if (playAgain.equals("Y") || playAgain.equals("y")) "Initial" else "Quit"
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

  def playRound(delay: Int): Boolean = {
    // do not play round if game is over
    if (scoreboard.highScore._2 >= 10) return true

    // local val for the number of tricks currently in the round
    val numTricks = gameArea.round.tricks.length
    if (numTricks != 0 && gameArea.round.tricks.last.cards.length != 4) {
      // in the middle of a round, make sure last trick is complete
      var currentTrick = gameArea.round.tricks.last
      for (p <- currentTrick.cards.length until 4) {
        // there are 4 players
        gameArea.playCard
        Thread.sleep(300)
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
        gameArea.playCard
        Thread.sleep(300)
      }
    }

    // check if end of game. return true if end of game, otherwise false
    if (scoreboard.highScore._2 >= 10) true
    else false
  }
}
