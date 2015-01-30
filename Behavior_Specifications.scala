describe("A Deck") {
	describe("in its initial state") {
		it("has 32 cards") { fail }
		it("has has 4 Aces") { fail }
		it("has has 4 Kings") { fail }
		it("has has 4 Queens") { fail }
		it("has has 4 Jacks") { fail }
		it("has has 4 10s") { fail }
		it("has has 4 9s") { fail }
		it("has has 4 8s") { fail }
		it("has has 4 7s") { fail }
		it("has has 4 Suits") { fail }
		it("can shuffle") { fail }
	}
	describe("in its ready state") {
		it("can deal card") { fail }
		it("can show top card") { fail }
		it("can shuffle") { fail }
	}
}

describe("A Hand") {
	describe("in its initial state") {
		it("has no cards") { fail }
	}
	describe("after cards have been dealt") {
		it("has 5 cards") { fail }
	}
	describe("during normal gameplay") {
		it("can play a card") { fail }
	}
}

describe("A Player") {
	describe("in its initial state") {
		it("has name") { fail }
		it("has teammate") { fail }
		it("has empty hand") { fail }
		it("has playing schema") { fail }
		it("has position") { fail }
		it("is dealer") { fail }
	}
	describe("in its ready to play state") {
		it("has 5 cards in hand") { fail }
		it("can play card") { fail }
		it("has a score of 0") { fail }
	}
	describe("once the first turn is over") {
		it("has 4 cards in hand") { fail }
	}
	describe("during normal gameplay") {
		it("can play card") { fail }
		it("can play trump") { fail }
		it("can follow suit") { fail }
		it("can win trick") { fail }
	}
}

describe("A team") {
	describe("in its initial state") {
		it("has 2 players") { fail }
		it("has 0 points") { fail }
	}
	describe("during normal gameplay") {
		it("has up to 7 points") { fail }
		it("has the deal") { fail }
	}
}

describe("A Trick") {
	describe("in its intial state") {
		it("has a leading player") { fail }
		it("has no cards") { fail }
	}
	describe("after first card has been played") {
		it("is the suit of trump") { fail }
		it("is a heart") { fail }
		it("is a club") { fail }
		it("is a spade") { fail }
		it("is a diamond") { fail }
	}
	describe("in its normal state") {
		it("has been trumped") { fail }
	}
	describe("after last player has played a card") {
		it("can declare winning player of the trick") { fail }
		it("can declare the winning team of the trick") { fail }
	}
}

describe("A Round") {
	describe("in its initial state") {
		it("has 2 teams") { fail }
		it("has played 0 tricks") { fail }
		it("has score of dealing team") { fail }
	}
	describe("during normal gameplay") {
		it("has up to 5 tricks") { fail }
		it("has dealing team score up to 5 tricks") { fail }
	}
	describe("5 tricks have been played") {
		it("declares winner of round") { fail }
		it("assigns points to winning team") { fail }
	}
}