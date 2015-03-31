package euchre

/**
 * Created by kyle on 2/11/15.
 */
class PlayerOrder(p1: Player, p2: Player, p3: Player, p4: Player) {
  private var _players: Array[Player] = Array(p1, p2, p3, p4)
  def players: Array[Player] = _players
  def players_(p:Array[Player]): Unit = (_players = p)

  def setPlayerOrder = {
    var leader = p1
    var indexOfLeader = 0
    for (p <- _players) {
      if (p.isLead) {
        leader = p
        indexOfLeader = players.indexOf(p)
      }
    }
    var newPlayerOrder = Array.empty[Player]
    for (i <- 0 until 4) {
      // 4 players
      newPlayerOrder = newPlayerOrder :+ players((indexOfLeader + i) % 4)
    }
    players_(newPlayerOrder)
  }

  override def toString() = p1.name + ", " + p2.name + ", " + p3.name + ", then " + p4.name
}
