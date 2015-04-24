package euchre

/**
 * Created by kyle on 3/30/15.
 */

import java.awt.Color

import scala.swing._

class View {
  object view {

    def main(controller: Controller): Unit = {
      val frame = new MainFrame

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
          })

          contents += new MenuItem(Action("Advance Player Order") {
            // advance player order
            // TODO:// Display the player order, or represent the current player up visually
            controller.advancePlayerOrder()
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
                            "\n\nCurrent " + controller.trick
          })

          contents += new MenuItem(Action("Simulate Round") {
            controller.playRound()
            // update the players' hands
            for((label,i) <- labels.zipWithIndex) {
              label.text = controller.playerName(i) + "'s " + controller.playerCards(i)
            }
            // update the game area
            textArea.text = controller.scoreboard.toString() + 
                            "\n\n" + controller.roundScoreboard +
                            "\n\n" + controller.trump + 
                            "\n\nCurrent " + controller.trick
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

            for (player <- 0 until playerOrder.length)
              contents += new Menu(playerOrder(player).toString()) {
                for (i <- 0 until schemas.length) {
                  contents += new MenuItem(Action(schemas(i)) {
                    // Set schema of player
                    controller.setSchema(playerOrder(player), new Schema(schemas(i)))
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

  def init(controller: Controller) = {
    view.main(controller)
  }
}
