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
        new TextArea{background = Color.gray},
        new TextArea{background = Color.red},
        new TextArea{background = Color.lightGray})

      for((label,i) <- labels.zipWithIndex) {
        label.text = "Player " + (i + 1) + "'s " + controller.playerCards(i).toString()
      }

      var textArea = new TextArea {
        text = "Welcome to Euchre\n"
        background = Color.green
      }

      val layoutRar = Array(BorderPanel.Position.North,
                            BorderPanel.Position.East,
                            BorderPanel.Position.South,
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
            controller.advancePlayerOrder()
          })

          contents += new MenuItem(Action("Play Card") {
            controller.playCard()
            for((label,i) <- labels.zipWithIndex) {
              label.text = "Player " + i + "'s " + controller.playerCards(i).toString()
            }
            textArea.text = controller.scoreboard.toString() + "\n\n\nCurrent " + controller.trick.toString()
          })

          contents += new MenuItem(Action("Simulate Round") {
            controller.playRound()
            for((label,i) <- labels.zipWithIndex) {
              label.text = "Player " + i + "'s " + controller.playerCards(i).toString()
            }
            textArea.text = controller.scoreboard.toString()
          })

          contents += new MenuItem(Action("Simulate Game") {
            controller.playGame()
            textArea.text = controller.scoreboard.toString()
          })
          contents += new Menu("Computer Settings") {
            var schemas = controller.schemas
            var playerOrder = controller.playerOrder

            for (player <- 0 until playerOrder.length)
              contents += new Menu(playerOrder(player).toString()) {
                for (i <- 0 until schemas.length) {
                  contents += new MenuItem(Action(schemas(i)) {
                    // Set schema of player
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

      frame.size = new Dimension(1000,1000)
      frame.centerOnScreen
      frame.visible = true
    }
  }

  def init(controller: Controller) = {
    view.main(controller)
  }
}
