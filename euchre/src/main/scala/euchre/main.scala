package euchre

object Main {
	def main(args: Array[String]): Unit = {
		println("Welcome to a game of Euchre!")

    /*
     * uncomment for text UI
     */
//		new GameMaster("Initial")

		/*
		 * uncomment for GUI
		 */
		val model = new Model()
		val view = new View
		val controller = new Controller(view, model)
		view.init(controller)
	}
}
