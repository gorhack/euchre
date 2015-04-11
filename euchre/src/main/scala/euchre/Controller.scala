package euchre

/**
 * Created by kyle on 3/30/15.
 */
class Controller(private var view: View, private var model: Model) {
  def init: Unit = {
    model.init
  }

  def playerOrder: Array[Player] = {
    model.playerOrder
  }

  def schemas: List[String] = {
    model.schemas
  }
}
