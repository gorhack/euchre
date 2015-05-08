package euchre

import java.awt.Color

import org.scalatest.{Matchers, FunSpec}

abstract class UnitSpec extends FunSpec with Matchers

class ProjectTester extends UnitSpec {
  var _deck = new Deck()

  private var _p1 = new Player(new Hand(List.empty), new Schema(""))
  private var _p2 = new Player(new Hand(List.empty), new Schema(""))
  private var _p3 = new Player(new Hand(List.empty), new Schema(""))
  private var _p4 = new Player(new Hand(List.empty), new Schema(""))
  private var _playerOrder = new PlayerOrder(_p1, _p3, _p2, _p4)
  private var _t1 = new Team(_p1, _p2)
  private var _t2 = new Team(_p3, _p4)
  private var _scoreboard = new Scoreboard(_playerOrder, _t1, _t2)
  private var _gameArea = new GameArea(_scoreboard, _t1, _t2, _playerOrder, _deck)

  describe("A Card Play") {
    _deck.init
    // Game Area
    // Players and Hand
    for (p <- _playerOrder.players) {
      p.init
      p.hand.init
    }
    _p1.isLead_(true)
    _playerOrder.setPlayerOrder
    _playerOrder.indexOfCurrentPlayer_(0)
    // Scoreboard
    _scoreboard.init
    // Team
    _t1.init
    _t2.init
    _gameArea.startNewRound

    _gameArea.deal
    _gameArea.setTrump

    // set the schemas
    _p1.schema_(new Schema("Aggressive"))
    _p2.schema_(new Schema("Passive"))
    _p3.schema_(new Schema("Aggressive-Trump"))
    _p4.schema_(new Schema("Passive-Fail"))

    // set the hands

    // A Heart, K Heart, Q Heart, 10 Heart, 9 Heart
    _p1.hand_(new Hand(List(new Card("A", 14, "Heart", Color.red), new Card("K", 13, "Heart", Color.red),
      new Card("Q", 12, "Heart", Color.red), new Card("10", 10, "Heart", Color.red),
      new Card("9", 9, "Heart", Color.red))))

    // A Diamond, K Diamond, Q Diamond, 10 Club, 9 Diamond
    _p2.hand_(new Hand(List(new Card("A", 14, "Diamond", Color.red), new Card("K", 13, "Diamond", Color.red),
      new Card("Q", 12, "Diamond", Color.red), new Card("10", 10, "Club", Color.black),
      new Card("9", 9, "Diamond", Color.red))))

    // A Heart, K Heart, Q Heart, 10 Heart, 9 Heart
    _p3.hand_(new Hand(List(new Card("A", 14, "Heart", Color.red), new Card("K", 13, "Heart", Color.red),
      new Card("Q", 12, "Heart", Color.red), new Card("10", 10, "Heart", Color.red),
      new Card("9", 9, "Heart", Color.red))))

    // A Diamond, K Diamond, Q Diamond, 10 Club, 9 Diamond
    _p4.hand_(new Hand(List(new Card("A", 14, "Diamond", Color.red), new Card("K", 13, "Diamond", Color.red),
      new Card("Q", 12, "Diamond", Color.red), new Card("10", 10, "Club", Color.black),
      new Card("9", 9, "Diamond", Color.red))))

    // set trump to clubs
    _gameArea.round.trump_("Club")
    _gameArea.round.color_(Color.black)

    describe("first trick") {
      it("plays first card") {
        _gameArea.playCard
        // player 1 leads with an aggressive schema; play the Ace of Hearts
        assert(_p1.hand.toString() == "Hand: (K Heart, Q Heart, 10 Heart, 9 Heart)")
      }
      it("plays next card") {
        _gameArea.playCard
        // player 3 follows suit with a aggressive-trump schema; play 9 Heart
        assert(_p3.hand.toString() == "Hand: (A Heart, K Heart, Q Heart, 10 Heart)")
      }
      it("plays 3rd card") {
        _gameArea.playCard
        // player 2 follows suit with a passive schema; play 10 Club
        assert(_p2.hand.toString() == "Hand: (A Diamond, K Diamond, Q Diamond, 9 Diamond)")
      }
      it("plays 4th card") {
        _gameArea.playCard
        // player 2 follows suit with a passive-fail schema; play 9 Diamond
        assert(_p4.hand.toString() == "Hand: (A Diamond, K Diamond, Q Diamond, 10 Club)")
      }
    }
  }
}