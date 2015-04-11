package euchre

/**
 * Created by kyle on 3/30/15.
 */

import scala.swing._

class View {
  object view {
    val textArea = new TextArea

    def main(controller: Controller): Unit = {
      val frame = new MainFrame

      frame.title = "Euchre"
      frame.contents = textArea
      controller.init

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
          })

          contents += new MenuItem(Action("Simulate Round") {
            controller.playRound()
          })

          contents += new MenuItem(Action("Simulate Game") {
            controller.playGame()
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

      frame.size = new Dimension(500, 500)
      frame.centerOnScreen
      frame.visible = true
    }
  }
  def init(controller: Controller) = {
    view.main(controller)
  }
}
