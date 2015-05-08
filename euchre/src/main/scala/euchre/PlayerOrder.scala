package euchre

/**
 * Created by kyle on 2/11/15.
 */
class PlayerOrder(private var p1: Player, private var p2: Player, private var p3: Player, private var p4: Player) {
  private var _players: Array[Player] = Array(p1, p2, p3, p4)
  def players: Array[Player] = _players
  def players_(p:Array[Player]): Unit = (_players = p)
  private var _indexOfCurrentPlayer = 0
  def indexOfCurrentPlayer: Int = _indexOfCurrentPlayer
  def indexOfCurrentPlayer_(i: Int): Unit = (_indexOfCurrentPlayer = i)

  override def toString() = p1.name + ", " + p2.name + ", " + p3.name + ", then " + p4.name
}
