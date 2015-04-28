package euchre

/**
 * Created by kyle on 4/27/15.
 */

import java.awt.{TextArea, Color}
import scala.swing.Swing.EmptyIcon
import scala.swing._
import scala.swing.BorderPanel.Position._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class GuiView extends View {
  private implicit val baseTime = System.currentTimeMillis()

  var gameArea = new Label("Welcome to Euchre", EmptyIcon, Alignment.Center)
  var userArea = new Array[Label](4)

  def init(_controller: Controller): Unit = {
    controller = Some(_controller)
    controller.get.init
    frame.menuBar = createMenu
    frame.size = new Dimension(1210,500)
    frame.centerOnScreen
    frame.visible = true
  }

  private val frame = new MainFrame {
    title = "Euchre"

    gameArea.text = "Welcome to Euchre"
    gameArea.background = Color.green

    createUserArea

    val layoutRar = Array(North, South, East, West)

    contents = new BorderPanel {
      layout += createTitle -> North
      layout += new BorderPanel {
        layout += gameArea -> Center
        for ((uArea, i) <- userArea.zipWithIndex) {
          layout += uArea -> layoutRar(i)
        }
      } -> Center
      layout += new GridPanel(1,3) {
        contents += new Button {
          text = "Play Card"
        }
        contents += new Button {
          text = "Play Round"
        }
        contents += new Button {
          text = "Play Game"
        }
      } -> South
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
    userArea = Array(new Label("", EmptyIcon, Alignment.Center),
                    new Label("", EmptyIcon, Alignment.Center),
                    new Label("", EmptyIcon, Alignment.Center),
                    new Label("", EmptyIcon, Alignment.Center))
    userArea(0).background = Color.cyan
    userArea(1).background = Color.cyan
    userArea(2).background = Color.LIGHT_GRAY
    userArea(3).background = Color.LIGHT_GRAY
  }

  def createMenu: MenuBar = {
    new MenuBar {
      contents += new Menu("File") {

        contents += new MenuItem(Action("New Game") {
          // init
          controller.get.init
          // clear all contents on screen
          // TODO:// when new game update names available
        })

        contents += new MenuItem(Action("Advance Player Order") {
          // advance player order
          // TODO:// Display the player order, or represent the current player up visually
          controller.get.advancePlayerOrder()
          gameArea.text = displayGameArea()
        })

        contents += new MenuItem(Action("Play Card") {
          // have the current player play a card
          val f = Future {
            controller.get.playCard()
          }

          // update the players' hands
          for ((uArea, i) <- userArea.zipWithIndex) {
            uArea.text = controller.get.playerName(i) + "'s " + controller.get.playerCards(i)
          }
          // update the game area
          gameArea.text = displayGameArea()
        })

        contents += new MenuItem(Action("Simulate Round") {
          val f = Future {
            controller.get.playRound(500)
          }
          // TODO:// add sleep timer
          // update the players' hands
          for ((uArea, i) <- userArea.zipWithIndex) {
            uArea.text = controller.get.playerName(i) + "'s " + controller.get.playerCards(i)
          }
          // update the game area
          gameArea.text = displayGameArea()
        })

        contents += new MenuItem(Action("Simulate Game") {
          // complete the game
          val f = Future {
            controller.get.playGame()
          }

          // update the game area text
          gameArea.text = controller.get.scoreboard.toString()
        })

        contents += new Menu("Computer Settings") {
          // update player schemas
          var schemas = controller.get.schemas
          var playerOrder = controller.get.playerOrder

          for (player <- 0 until playerOrder.players.length)
            contents += new Menu(playerOrder.players(player).toString()) {
              for (i <- 0 until schemas.length) {
                contents += new MenuItem(Action(schemas(i)) {
                  // Set schema of player
                  controller.get.setSchema(playerOrder.players(player), new Schema(schemas(i)))
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

  def displayPlayerOrder(_playerOrder: PlayerOrder): Unit = {

  }

  def displayCurrentPlayer(_player: Player): Unit = {

  }

  def displayTrick(_trick: Trick): Unit = {

  }

  def displayWinner(_player: Player): Unit = {

  }

  def displayGameArea(): String = {
      "<html><br><br>" + controller.get.scoreboard.toString() +
      "<br><br>" + controller.get.roundScoreboard +
      "<br><br>" + controller.get.trump +
      "<br><br>" + "Round Player Order: " + controller.get.advancePlayerOrder().toString() +
      "<br><br>Current" + controller.get.trick + "<br><br></html>"
  }
}
