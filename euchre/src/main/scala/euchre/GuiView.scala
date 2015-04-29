package euchre

/**
 * Created by kyle on 4/27/15.
 */

import java.awt.{TextArea, Color}
import javax.swing.ImageIcon
import scala.swing.Swing.EmptyIcon
import scala.swing._
import scala.swing.BorderPanel.Position._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.swing.event.ButtonClicked

class GuiView extends View {
  private implicit val baseTime = System.currentTimeMillis()
  private val delay = 1000
  private var gameOver = false

  var gameArea = new Label("Welcome to Euchre", EmptyIcon, Alignment.Center)
  var userArea = new Array[Label](4)
  var initialPlayerOrder = new Array[Player](4)

  val playCardButton = new Button {text = "Play Card"}
  val playRoundButton = new Button {text = "Play Round"}
  val playGameButton = new Button {text = "Play Game"}

  def init(_controller: Controller): Unit = {
    gameOver = false
    controller = Some(_controller)
    controller.get.init
    initialPlayerOrder = controller.get.playerOrder.players
    frame.menuBar = createMenu
    frame.size = new Dimension(1210,500)
    frame.centerOnScreen
    frame.visible = true

    // wait for "loading screen"
    Thread.sleep(delay)
    // load game
    displayPlayers()
    setNextPlayer(controller.get.playerOrder.indexOfCurrentPlayer)
    displayGameArea(controller.get.scoreboard, controller.get.round, controller.get.playerOrder, controller.get.trick)
  }

  private val frame = new MainFrame {
    title = "Euchre"

    gameArea.text = "Welcome to Euchre"
    gameArea.background = Color.green

    createUserArea

    val layoutRar = Array(North, East, South, West)

    contents = new BorderPanel {
      layout += createTitle -> North
      layout += new BorderPanel {
        layout += gameArea -> Center
        for ((uArea, i) <- userArea.zipWithIndex) {
          layout += uArea -> layoutRar(i)
        }} -> Center
      layout += new GridPanel(1,3) {
        contents += playCardButton
        contents += playRoundButton
        contents += playGameButton} -> South
    }

    listenTo(playCardButton)
    listenTo(playRoundButton)
    listenTo(playGameButton)

    reactions += {
      case ButtonClicked(component) if component == playCardButton =>
        val f = Future {
          controller.get.playCard()
        }
      case ButtonClicked(component) if component == playRoundButton =>
        val f = Future {
          controller.get.playRound(delay)
        }
      case ButtonClicked(component) if component == playGameButton =>
        val f = Future {
          controller.get.playGame(delay)
        }
    }
  }

  def createTitle: Label = {
      new Label("Euchre Simulation, by Kyle Gorak. v1.0", EmptyIcon, Alignment.Center)

    // how to create image on panel:
    //    val image = javax.imageio.ImageIO.read(new java.io.File("./images/banner.jpg")): BufferedImage
    //
    //    new Panel {
    //      override def paint(g: Graphics2D) {
    //        g.drawImage(image, 0, 0, null)
    //      }
    //    }
  }

  def createUserArea: Unit = {
    // set user areas
    userArea = Array.fill[Label](4)(new Label("", EmptyIcon, Alignment.Center))
    userArea(0).foreground = Color.cyan
    userArea(2).foreground = Color.cyan
    userArea(1).foreground = Color.green
    userArea(3).foreground = Color.green
  }

  def createMenu: MenuBar = {
    new MenuBar {
      contents += new Menu("File") {

        contents += new MenuItem(Action("New Game") {
          // init
          gameArea.text = "Welcome to Euchre...starting new game"
          for (uArea <- userArea) uArea.text = ""
          val f = Future {
            controller.get.init
            init(controller.get)
          }
        })

        contents += new MenuItem(Action("Advance Player Order") {
          // advance player order
          if (!gameOver) {
            val f = Future {
              controller.get.advancePlayerOrder()
            }
          }
        })

        contents += new MenuItem(Action("Play Card") {
          // have the current player play a card
          if (!gameOver) {
            val f = Future {
              controller.get.playCard()
            }
          }
        })

        contents += new MenuItem(Action("Simulate Round") {
          if (!gameOver) {
            val f = Future {
              controller.get.playRound(delay)
            }
          }
        })

        contents += new MenuItem(Action("Simulate Game") {
          // complete the game
          if (!gameOver) {
            val f = Future {
              controller.get.playGame(delay)
            }
          }
        })

        contents += new Menu("Computer Settings") {
          // update player schemas
          var schemas = controller.get.schemas
          var playerOrder = controller.get.playerOrder

          for (player <- 0 until initialPlayerOrder.length)
            contents += new Menu(initialPlayerOrder(player).toString()) {
              for (i <- 0 until schemas.length) {
                contents += new MenuItem(Action(schemas(i)) {
                  // Set schema of player
                  controller.get.setSchema(initialPlayerOrder(player), new Schema(schemas(i)))
                })
              }
            }
        }
        contents += new Separator

        contents += new MenuItem(Action("Exit") {
          sys.exit(0)
        }) // end Exit menuItem

      } // end File menu
    } // end MenuBar
  }

  def displayPlayers(): Unit = {
    // update the players' hands
    for ((uArea, i) <- userArea.zipWithIndex) {
      uArea.text = initialPlayerOrder(i) + "'s " + initialPlayerOrder(i).hand
    }
  }

  def setNextPlayer(_indexOfCurrentPlayer: Int): Unit = {
    for ((p, i) <- userArea.zipWithIndex) {
      if (i == _indexOfCurrentPlayer % 4) {
        p.icon = new ImageIcon("star.png")
      }
      else p.icon = EmptyIcon
    }
  }

  def displayWinner(s: String): Unit = {
    gameOver = true
    // update the game area text
    gameArea.text = s
    for (uArea <- userArea) uArea.text = ""

  }

  def displayGameArea(_scoreboard: Scoreboard, _round: Round, _playerOrder: PlayerOrder, _trick: String): Unit = {
    // update game area
    gameArea.text = "<html><br><br>" + _scoreboard.toString() +
                    "<br><br>Round Score is: " + _round.roundScore._1 + " to " + _round.roundScore._2 +
                    "<br><br>Round Tump is: " + _round.trump.toString() +
                    "<br><br>Round Player Order: " + _playerOrder.toString() +
                    "<br><br>Current" + _trick + "<br><br></html>"
  }
}
