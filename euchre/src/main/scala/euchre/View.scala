package euchre

/**
 * Created by kyle on 3/30/15.
 */

import java.awt.Color
import concurrent.Future
import scala.swing._

class View {
    def init(controller: Controller): Unit = {
      lazy val frame = new MainFrame

      frame.title = "Euchre"
      controller.init

      var labels = Array(
        new TextArea{background = Color.cyan},
        new TextArea{background = Color.cyan},
        new TextArea{background = Color.lightGray},
        new TextArea{background = Color.lightGray})

      for((label,i) <- labels.zipWithIndex) {
        label.text = controller.playerName(i) + "'s " + controller.playerCards(i)
      }

      var textArea = new TextArea {
        text = "Welcome to Euchre\n\n" + controller.roundScoreboard + "\n\n\n" + controller.trump
        background = Color.green
      }

      val layoutRar = Array(BorderPanel.Position.North,
                            BorderPanel.Position.South,
                            BorderPanel.Position.East,
                            BorderPanel.Position.West)

      frame.contents = new BorderPanel {
        layout(textArea) = BorderPanel.Position.Center
        for ((label, i) <- labels.zipWithIndex) {
          layout(label) = layoutRar(i)
        }
      }

      frame.menuBar = new MenuBar {

        contents += new Menu("File") {

          contents += new MenuItem(Action("New Game") {
            // init
            controller.init
            // clear all contents on screen
            // TODO:// when new game update names available
          })

          contents += new MenuItem(Action("Advance Player Order") {
            // advance player order
            // TODO:// Display the player order, or represent the current player up visually
            controller.advancePlayerOrder()
            textArea.text = controller.scoreboard.toString() +
                            "\n\n" + controller.roundScoreboard +
                            "\n\n" + controller.trump +
                            "\n\n" + "Round Player Order: " + controller.playerOrder.toString() +
                            "\n\nCurrent" + controller.trick
          })

          contents += new MenuItem(Action("Play Card") {
            // have the current player play a card
            controller.playCard()
            // update the players' hands
            for((label,i) <- labels.zipWithIndex) {
              label.text = controller.playerName(i) + "'s " + controller.playerCards(i)
            }
            // update the game area
            textArea.text = controller.scoreboard.toString() + 
                            "\n\n" + controller.roundScoreboard +
                            "\n\n" + controller.trump +
                            "\n\n" + "Round Player Order: " + controller.playerOrder.toString() +
                            "\n\nCurrent" + controller.trick
          })

          contents += new MenuItem(Action("Simulate Round") {
            controller.playRound(1000)
            // TODO:// add sleep timer
            // update the players' hands
            for((label,i) <- labels.zipWithIndex) {
              label.text = controller.playerName(i) + "'s " + controller.playerCards(i)
            }
            // update the game area
            textArea.text = controller.scoreboard.toString() +
                            "\n\n" + controller.roundScoreboard +
                            "\n\n" + controller.trump +
                            "\n\n" + "Round Player Order: " + controller.playerOrder.toString() +
                            "\n\nCurrent" + controller.trick
          })

          contents += new MenuItem(Action("Simulate Game") {
            // complete the game
            controller.playGame()
            // update the game area text
            textArea.text = controller.scoreboard.toString()
          })
          contents += new Menu("Computer Settings") {
            // update player schemas
            var schemas = controller.schemas
            var playerOrder = controller.playerOrder

            for (player <- 0 until playerOrder.players.length)
              contents += new Menu(playerOrder.players(player).toString()) {
                for (i <- 0 until schemas.length) {
                  contents += new MenuItem(Action(schemas(i)) {
                    // Set schema of player
                    controller.setSchema(playerOrder.players(player), new Schema(schemas(i)))
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

      frame.size = new Dimension(1200,400)
      frame.centerOnScreen
      frame.visible = true
    }
}
