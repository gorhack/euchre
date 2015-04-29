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

  def setPlayerOrder: Unit = {
    // find the leader, reset player order based on the leader
    var leader = p1
    var indexOfLeader = 0
    for (p <- _players) {
      if (p.isLead) {
        leader = p
        indexOfLeader = players.indexOf(p)
      }
    }
    var newPlayerOrder = new Array[Player](4)
    for (i <- 0 until 4) {
      // 4 players
      newPlayerOrder(i) = players((indexOfLeader + i) % 4)
    }
    players_(newPlayerOrder)
  }

  def advancePlayerOrder: PlayerOrder = {
    val temp1 = p1
    p1 = p2
    p2 = p3
    p3 = p4
    p4 = temp1

    this
  }

  override def toString() = p1.name + ", " + p2.name + ", " + p3.name + ", then " + p4.name
}
