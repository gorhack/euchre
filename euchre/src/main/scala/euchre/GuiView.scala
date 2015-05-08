package euchre

/**
 * Created by kyle on 4/27/15.
 */

import java.awt.image.BufferedImage
import java.awt.{TextArea, Color}
import javax.swing.ImageIcon
import javax.swing.border.{MatteBorder, LineBorder, Border}
import scala.swing.Swing.EmptyIcon
import scala.swing._
import scala.swing.BorderPanel.Position._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.swing.event.ButtonClicked

class GuiView extends View {
  private implicit val baseTime = System.currentTimeMillis()

  // constants
  private val WIDTH = 1216
  private val HEIGHT = 822
  private val DELAY = 200
  private var gameOver = false
  private var SHOWHANDS = true
  private val CARD_SIZE = new Dimension(71, 96)

  // game area colors
  private val GAME_AREA_COLOR = new Color(85,167,83)
  // panels for the game area
  private var scorePanel = new Label("")
  private var trumpPanel = new Label("") { preferredSize = new Dimension(284, 96) }
  private var trickImg = new Array[Label](4)
  private var trickPanel = new GridPanel(1,4) { preferredSize = new Dimension(284, 96) }

  // the game area panel
  private var gameArea = new BorderPanel() { preferredSize = new Dimension(284, 246) }

  // user area colors
  private val USER_AREA_COLOR = new Color(183,182,172)
  private val TEAM_1_COLOR = new Color(84,76,255)
  private val TEAM_2_COLOR = new Color(189,33,129)
  // panels for the user area
  private var namePanel = Array.fill[Label](4)(new Label())
  private var handImg = new Array[Array[Label]](4)
  private var handPanel = Array.fill[GridPanel](4)(new GridPanel(1, 5))
  private var turnPanel = Array.fill[Label](4)(new Label())

  // the user area panels
  private var player1Area = new BorderPanel() { preferredSize = new Dimension(WIDTH, 168) }
  private var player3Area = new BorderPanel() { preferredSize = new Dimension(WIDTH, 168) }
  private var player2Area = new BorderPanel() { preferredSize = new Dimension(405, 246) }
  private var player4Area = new BorderPanel() { preferredSize = new Dimension(405, 246) }

  // the player order
  private var initialPlayerOrder = new Array[Player](4)

  private val playCardButton = new Button {text = "Play Card"}
  private val playRoundButton = new Button {text = "Play Round"}
  private val playGameButton = new Button {text = "Play Game"}

  def init(_controller: Controller): Unit = {
    gameOver = false
    controller = Some(_controller)
    controller.get.init
    initialPlayerOrder = controller.get.playerOrder.players
    frame.menuBar = createMenu
    frame.size = new Dimension(WIDTH, HEIGHT)
    frame.centerOnScreen
    frame.visible = true

    // wait for "loading screen"
    Thread.sleep(DELAY)
    // load game
    setNextPlayer(controller.get.playerOrder.indexOfCurrentPlayer)
    displayGameArea(controller.get.scoreboard, controller.get.round, controller.get.playerOrder, controller.get.trick)
    displayPlayers()
  }

  private val frame = new MainFrame {
    title = "Euchre"

    scorePanel.text = "Welcome to Euchre"

    createUserArea
    createGameArea

    // create the main frame
    contents = new BorderPanel {
      layout += createTitle -> North
      layout += new BorderPanel {
        layout += gameArea -> Center
        layout += player1Area -> North
        layout += player2Area -> East
        layout += player3Area -> South
        layout += player4Area -> West
      } -> Center
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
          controller.get.playRound(DELAY)
        }
      case ButtonClicked(component) if component == playGameButton =>
        val f = Future {
          controller.get.playGame(DELAY)
        }
    }
  }

  def createTitle: Panel = {
    // create image on panel
    val image = javax.imageio.ImageIO.read(new java.io.File("./Images/euchreHeader.jpg")): BufferedImage

    var panel = new Panel {
      override def paint(g: Graphics2D) {
        g.drawImage(image, 0, 0, null)
      }
    }
    panel.preferredSize = new Dimension(WIDTH, 180)
    panel
  }

  def createGameArea: Unit = {
    // set the game areas
    trickPanel.background = GAME_AREA_COLOR

    // set up cards for trick
    for (i <- 0 until 4) { trickImg(i) = new Label() { preferredSize = CARD_SIZE } }

    // add the cards to the trick panel
    trickPanel.contents += trickImg(0)
    trickPanel.contents += trickImg(1)
    trickPanel.contents += trickImg(2)
    trickPanel.contents += trickImg(3)

    // add game area components to game area
    gameArea.layout += scorePanel -> North
    gameArea.layout += trumpPanel -> Center
    gameArea.layout += trickPanel -> South

    // set background colors
    gameArea.background = GAME_AREA_COLOR
  }

  def createUserArea: Unit = {
    // set up cards for hand
    for (i <- 0 until 4) { handImg(i) = Array.fill[Label](5)(new Label()) }

    // add the cards to the hand panel
    for (i <- 0 until 4) {
      handPanel(i).contents += handImg(i)(0)
      handPanel(i).contents += handImg(i)(1)
      handPanel(i).contents += handImg(i)(2)
      handPanel(i).contents += handImg(i)(3)
      handPanel(i).contents += handImg(i)(4)
    }

    // set up player 1
    namePanel(0).foreground = TEAM_1_COLOR
    turnPanel(0).preferredSize = new Dimension(282, 56)

    player1Area.layout += namePanel(0) -> North
    player1Area.layout += handPanel(0) -> Center
    player1Area.layout += turnPanel(0) -> South

    // set up player 2
    namePanel(1).foreground = TEAM_2_COLOR
    turnPanel(1).preferredSize = new Dimension(50, 250)

    player2Area.layout += namePanel(1) -> North
    player2Area.layout += handPanel(1) -> Center
    player2Area.layout += turnPanel(1) -> West

    // set up player 3
    namePanel(2).foreground = TEAM_1_COLOR
    turnPanel(2).preferredSize = new Dimension(282, 56)

    player3Area.layout += turnPanel(2) -> North
    player3Area.layout += handPanel(2) -> Center
    player3Area.layout += namePanel(2) -> South

    // set up player 4
    namePanel(3).foreground = TEAM_2_COLOR
    turnPanel(3).preferredSize = new Dimension(50, 250)

    player4Area.layout += namePanel(3) -> North
    player4Area.layout += handPanel(3) -> Center
    player4Area.layout += turnPanel(3) -> East

    // set background colors
    player1Area.background = USER_AREA_COLOR
    player2Area.background = USER_AREA_COLOR
    player3Area.background = USER_AREA_COLOR
    player4Area.background = USER_AREA_COLOR

    for (i <- 0 until 4) { handPanel(i).background = USER_AREA_COLOR }
  }

  def createMenu: MenuBar = {
    new MenuBar {
      contents += new Menu("File") {

        contents += new MenuItem(Action("New Game") {
          // init
          scorePanel.text = "Welcome to Euchre...starting new game"

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
              controller.get.playRound(DELAY)
            }
          }
        })

        contents += new MenuItem(Action("Simulate Game") {
          // complete the game
          if (!gameOver) {
            val f = Future {
              controller.get.playGame(DELAY)
            }
          }
        })

        contents += new Menu("Computer Settings") {
          // update player schemas
          var schemas = controller.get.schemas
          var playerOrder = controller.get.playerOrder

          for (player <- 0 until initialPlayerOrder.length) {
            contents += new Menu(initialPlayerOrder(player).toString()) {
              for (i <- 0 until schemas.length) {
                contents += new MenuItem(Action(schemas(i)) {
                  // Set schema of player
                  val f = Future {
                    controller.get.setSchema(initialPlayerOrder(player), new Schema(schemas(i)))
                  }
                })
              }
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
    // set user names
    for (i <- 0 until 4) { namePanel(i).text = initialPlayerOrder(i).name + ": " + initialPlayerOrder(i).schema}
    // update hand
    if (SHOWHANDS) {
      for (i <- 0 until 4) {
        initialPlayerOrder(i).hand.cards.length match {
          case 5 => {
            for (j <- 0 until 5) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(i).hand.cards(j).fileName)
            }
          }
          case 4 => {
            for (j <- 0 until 4) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(i).hand.cards(j).fileName)
            }
            for (j <- 4 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
          case 3 => {
            for (j <- 0 until 3) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(i).hand.cards(j).fileName)
            }
            for (j <- 3 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
          case 2 => {
            for (j <- 0 until 2) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(i).hand.cards(j).fileName)
            }
            for (j <- 2 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
          case 1 => {
            for (j <- 0 until 1) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(i).hand.cards(j).fileName)
            }
            for (j <- 1 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
          case 0 => {
            for (j <- 0 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
        }
      }
    }
    else {
      initialPlayerOrder(0).hand.cards.length match {
        case 5 => {
          for (j <- 0 until 5) {
            handImg(0)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(0).hand.cards(j).fileName)
          }
        }
        case 4 => {
          for (j <- 0 until 4) {
            handImg(0)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(0).hand.cards(j).fileName)
          }
          for (j <- 4 until 5) {
            handImg(0)(j).icon = EmptyIcon
          }
        }
        case 3 => {
          for (j <- 0 until 3) {
            handImg(0)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(0).hand.cards(j).fileName)
          }
          for (j <- 3 until 5) {
            handImg(0)(j).icon = EmptyIcon
          }
        }
        case 2 => {
          for (j <- 0 until 2) {
            handImg(0)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(0).hand.cards(j).fileName)
          }
          for (j <- 2 until 5) {
            handImg(0)(j).icon = EmptyIcon
          }
        }
        case 1 => {
          for (j <- 0 until 1) {
            handImg(0)(j).icon = new ImageIcon("./cards_png/" + initialPlayerOrder(0).hand.cards(j).fileName)
          }
          for (j <- 1 until 5) {
            handImg(0)(j).icon = EmptyIcon
          }
        }
        case 0 => {
          for (j <- 0 until 5) {
            handImg(0)(j).icon = EmptyIcon
          }
        }
      }
      for (i <- 1 until 4) {
        initialPlayerOrder(i).hand.cards.length match {
          case 5 => {
            for (j <- 0 until 5) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/b2fv.png")
            }
          }
          case 4 => {
            for (j <- 0 until 4) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/b2fv.png")
            }
            for (j <- 4 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
          case 3 => {
            for (j <- 0 until 3) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/b2fv.png")
            }
            for (j <- 3 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
          case 2 => {
            for (j <- 0 until 2) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/b2fv.png")
            }
            for (j <- 2 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
          case 1 => {
            for (j <- 0 until 1) {
              handImg(i)(j).icon = new ImageIcon("./cards_png/b2fv.png")
            }
            for (j <- 1 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
          case 0 => {
            for (j <- 0 until 5) {
              handImg(i)(j).icon = EmptyIcon
            }
          }
        }
      }
    }
  }

  def setNextPlayer(_indexOfCurrentPlayer: Int): Unit = {
    for (i <- 0 until 4) {
      if (i == (_indexOfCurrentPlayer % 4)) {
        turnPanel(i).icon = new ImageIcon("./Images/p" + (i + 1) + "Turn.png")
      }
      else {
        turnPanel(i).icon = EmptyIcon
      }
    }
  }

  def displayWinner(s: String): Unit = {
    gameOver = true
    // update the game area text
    scorePanel.text = s
    trumpPanel.text = ""
    trumpPanel.icon = EmptyIcon
    for (i <- 0 until 4) { trickImg(i).icon = EmptyIcon }

    for (i <- 0 until 4) {
      turnPanel(i).icon = EmptyIcon
      namePanel(i).text = ""
      for (j <- 0 until 5) {
        handImg(i)(j).icon = EmptyIcon
      }
    }
  }

  def displayGameArea(_scoreboard: Scoreboard, _round: Round, _playerOrder: PlayerOrder, _trick: String): Unit = {
    // update game area
    scorePanel.text = "<html>" + _scoreboard.toString() +
                      "<br>Round Score is: " + _round.roundScore._1 + " to " + _round.roundScore._2 +
                      "</html>"
    trumpPanel.text = " is Trump"
    trumpPanel.icon = new ImageIcon("./cards_png/" + _round.trump.toString() + ".png")
    if (_round.tricks.size != 0 && _round.tricks.last.cards.size != 0) {
      _round.tricks.last.cards.length match {
        case 1 => {
          trickImg(0).icon = new ImageIcon("./cards_png/" + _round.tricks.last.cards.head.fileName)
          trickImg(1).icon = EmptyIcon
          trickImg(2).icon = EmptyIcon
          trickImg(3).icon = EmptyIcon
        }
        case 2 => {
          trickImg(1).icon = new ImageIcon("./cards_png/" + _round.tricks.last.cards.tail.head.fileName)
        }
        case 3 => {
          trickImg(2).icon = new ImageIcon("./cards_png/" + _round.tricks.last.cards.tail.tail.head.fileName)
        }
      }
    }
    else if (_round.tricks.size > 1 && _round.tricks.last.cards.size == 0) {
      trickImg(3).icon = new ImageIcon("./cards_png/" + _round.tricks(_round.tricks.length-2).cards.last.fileName)
    }
    else {
      trickImg(0).icon = EmptyIcon
      trickImg(1).icon = EmptyIcon
      trickImg(2).icon = EmptyIcon
      trickImg(3).icon = EmptyIcon
    }
  }
}
