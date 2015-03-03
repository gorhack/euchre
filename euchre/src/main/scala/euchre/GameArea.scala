/**
 * Created by kyle on 2/10/15.
 */
package euchre

class GameArea(private var _scoreboard: Scoreboard, private var _t1: Team,
               private var _t2: Team, private var _playerOrder: PlayerOrder,
                private var deck: Deck) {
  private var _round = new Round()
  def round: Round = _round
  def round_(r:Round):Unit = (_round = r)
  def updateScorebaord: Scoreboard = _scoreboard
  def displayScoreboard: Scoreboard = _scoreboard
  def scoreboard_(s:Scoreboard):Unit = (_scoreboard = s)
  def startNewRound = _round.init
  // set scoreboard
  def setScoreboard = {

  }
  // set dealer
  def setLead = {

  }
  // give players cards
  def deal = {
    for (p <- _playerOrder.players) {
      for (i <- 0 until 5) {
        p.hand_(new Hand(p.hand.cards :+ deck.deal))
      }
      println(p.name + "'s Hand: " + p.hand.sortHand)
    }
  }
  def setTrump = {
    val t = deck.showTopCard.suit
    round.trump_(t)
    println("Trump for the round is " + t + "s")
  }
  // play cards to trick
  def playCards = {
    for (t <- 0 until 5) {
      var trick = new Trick()
      for (p <- _playerOrder.players) {
        // TODO:// Follow rules...
        trick.cards_(trick.cards :+ p.playCard("",""))
      }
      println("Trick " + (t + 1) + ": " + trick)
      decideWinnerOfTrick
      round.tricks :+ trick
    }
  }
  // decide winner of tricks
  def decideWinnerOfTrick = {
    // TODO:// Decide actual winner
    _t1.points_(_t1.points + 1)
    updateScorebaord
  }
  // update scoreboard
  def updateScoreboard = {
    _scoreboard.scores_(_t1.points, _t2.points)
    println(_scoreboard.toString())
  }
}
