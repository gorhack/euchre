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

      frame.title = "My GUI"
      frame.contents = textArea

      frame.menuBar = new MenuBar {

        contents += new Menu("Menu1") {

          contents += new MenuItem(Action("Hello") {
            textArea.text = "Hello World"
          })

          contents += new MenuItem("todo")
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
