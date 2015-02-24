package euchre

/**
 * Created by kyle on 2/11/15.
 */
class PlayerOrder(p1: Player, p2: Player, p3: Player, p4: Player) {
  def players: Array[Player] = Array(p1, p2, p3, p4)
  override def toString() = p1.name + ", " + p2.name + ", " + p3.name + ", then " + p4.name
}
