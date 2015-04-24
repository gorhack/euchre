package euchre

import java.awt.Color

import org.scalatest.{Matchers, FunSpec}

abstract class UnitSpec extends FunSpec with Matchers

class ProjectTester extends UnitSpec {
  val gm = new GameMaster("Start")
  val hand = new Hand(List.empty)
  val schema = new Schema("")
  val p1 = new Player(hand, schema)
  val p2 = new Player(hand, schema)
  val p3 = new Player(hand, schema)
  val p4 = new Player(hand, schema)
  val t1 = new Team(p1, p2)
  val t2 = new Team(p3, p4)
  val playerOrder = new PlayerOrder(p1, p2, p3, p4)
  val scoreboard = new Scoreboard(playerOrder, t1, t2)
  describe("A Deck") {
    val deck = new Deck
    if (gm.state == "Start") {
      describe("in its initial state") {
        it("has 32 cards") {
          assert(deck.length == 32)
        }
      }
    }
    if (gm.state == "Play")
    describe("in its ready state") {
      it("can deal card") {
        //deck.deal should be (true)
      }
//      it("can show top card") {
//        val aCard = new Card('A', "Heart")
//        deck.showTopCard should be (aCard) // cannot compare new objects...
//      }
      it("can shuffle") {
        //deck.shuffle should be (true)
      }
    }
    describe("in its play state") { // during a round
      it("has 20 cards") {
        assert(deck.length == 20)
      }
    }
    describe("after round completed") {
      it("is initialized") {
        val newDeck = new Deck
        deck.init should equal(newDeck)
      }
    }
  }

  describe("A Card") {
    val card = new Card("A", 14, "Heart", Color.red)
    it("has value") {
      assert(card.value == ("A", "K", "Q", "J", "10", "9", "8", "7"))
    }
    it("has suit") {
      assert(card.suit == ("Club", "Spade", "Diamond", "Heart"))
    }
  }


  describe("A Hand") {
    val hand = new Hand(List.empty)
    describe("in its initial state") {
      it("has no cards") {
        hand.length should be (0)
      }
    }
    describe("after cards have been dealt") {
      it("has 5 cards") {
        hand.length should be (0)
      }
    }
    describe("during normal gameplay") {
      it("can play a card") {
        //hand.playCard should be (true)
      }
    }
    describe("after round completed") {
      it("is has no cards") {
        //hand.length should be (0)
      }
    }
  }

  describe("A Player") {
    describe("in its initial state") {
      it("has name") {
        p1.name.length should be > 0
      }
      it("has teammate") {
        val aTeammate = new Player(hand, schema)
        //p1.teammate should be (aTeammate)
      }
      it("has empty hand") {
        assert(p1.hand.length == 0)
      }
      it("has playing schema") {
        p1.schema should be (schema)
      }
      it("has position") {
        //p1.position should be (playerOrder)
      }
      it("is dealer") {
        p1.isLead should be (true)
      }
    }
    describe("in its ready to play state") {
      it("has 5 cards in hand") {
        p1.hand.length shouldEqual 5
      }
      it("can play card") {
        p1.canPlayCard should be (true)
      }
    }
    describe("once the first turn is over") {
      it("has 4 cards in hand") {
        p1.hand.length shouldEqual 4
      }
    }
    describe("during normal gameplay") {
      it("can play card") {
        p1.canPlayCard should be (true)
      }
      it("can play trump") {
        p1.canPlayCard should be (true)
      }
      it("can follow suit") {
        p1.canPlayCard should be (true)
      }
    }
  }

  describe("A team") {
    describe("in its initial state") {
      it("has 2 players") {
        t1.size shouldEqual 2
      }
      it("has 0 points") {
        t1.points shouldEqual 0
      }
    }
    describe("during normal gameplay") {
      it("has 7 or fewer points") {
        t1.points should be <= 7
      }
      it("has the deal") {
        t1.hasDeal should be (true)
      }
      it("has between 0 and 5 tricks") {
        t1.tricks should be <= 5
      }
    }
  }

  describe("A Trick") {
    val trick = new Trick
    describe("in its intial state") {
      it("has a leading player") {
        trick.leader should be (p1)
      }
      it("has no cards") {
        trick.cards.length shouldEqual 0
      }
    }
    describe("after first card has been played") {
      it("is the suit of trump") {
        val round = new Round
        trick.cards(0).suit shouldEqual round.trump
      }
    }
    describe("in its normal state") {
      it("has been trumped") {
        trick.isTrumped should be (true)
      }
    }
    describe("after last player has played a card") {
      it("can declare winning player of the trick") {
        val winningPlayer = new Player(hand, schema)
        trick.winner should be (winningPlayer)
      }
      it("can declare the winning team of the trick") {
        val aTeam = new Team(new Player(hand, schema), new Player(hand, schema))
        trick.winningTeam should be (aTeam)
      }
    }
  }

  describe("A Round") {
    val round = new Round
    describe("in its initial state") {
      it("has 2 teams") {
        //round.teams.length shouldEqual 2
      }
      it("has played 0 tricks") {
        round.tricks.length shouldEqual 0
      }
      it("has score of dealing team") {
        val dealingTeam = new Team(new Player(hand, schema), new Player(hand, schema))
        //round.highScore should be ((dealingTeam, 0))
      }
    }
    describe("during normal gameplay") {
      it("has dealing team score up to 5 tricks") {
        //round.highScore._2 should be <= 5
      }
    }
  }

  describe("A Trump") {
    val trump = new Trump
    describe("in its initial state") {
      it("is not set") {
        trump.suit should be (null)
      }
    }
    describe("during normal gameplay") {
      it("has suit") {
        val deck = new Deck
        trump.suit should be (deck.showTopCard.suit)
      }
    }
  }

  describe("A Scoreboard") {
    describe("in its initial state") {
      it("all scores are zero") {
        scoreboard.scores._1 shouldEqual 0
        scoreboard.scores._2 shouldEqual 0
      }
    }
    describe("in its normal state") {
      it("has high score >= 0") {
        scoreboard.highScore._2 should be >= 0
      }
      it("high score is not greater than 7") {
        scoreboard.highScore._2 should be <=7
      }
    }
  }

  describe("A GameArea") {
    val gameArea = new GameArea(scoreboard, t1, t2, playerOrder, new Deck())
    describe("in its initial state") {
      it("has scoreboard") {
        //gameArea.displayScoreboard should be (scorebaord)
      }
      it("has round") {
        val round = new Round
        gameArea.round should be (round)
      }
    }
    describe("after round complete") {
      it("updates scorebaord") {
        //gameArea.updateScorebaord should be (scorebaord)
      }
      it("displays scoreboard") {
        gameArea.displayScoreboard should be (scoreboard)
      }
      it("starts another round") {
        gameArea.startNewRound should be (scoreboard)
      }
    }
  }
}