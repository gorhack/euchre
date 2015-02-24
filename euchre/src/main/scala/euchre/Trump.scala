package euchre

/**
 * Created by kyle on 2/11/15.
 */
class Trump {
  private var _suit = ""
  def suit: String = _suit
  def suit_(s:String): Unit = (_suit = s)
}
