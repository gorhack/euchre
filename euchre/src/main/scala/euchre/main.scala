package euchre

object Main {
	def main(args: Array[String]): Unit = {
		println("Welcome to a game of Euchre!")

    //val model = new GameMaster("Initial")
		val model = new Model()
		val view = new View
		val controller = new Controller(view, model)
		view.init(controller)
	}
}
