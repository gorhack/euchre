/**
 * Created by kyle on 2/10/15.
 */
package euchre

import java.awt.Color

import util.Random

class Deck {
  /*
   * Deck contains 32 cards
   */
  private val newDeck = List(
    new Card("A", 14, "Heart", Color.red, "h1.png"), new Card("A", 14, "Diamond", Color.red, "d1.png"), new Card("A", 14, "Club", Color.black, "c1.png"), new Card("A", 14, "Spade", Color.black, "s1.png"),
    new Card("K", 13, "Heart", Color.red, "hk.png"), new Card("K", 13, "Diamond", Color.red, "dk.png"), new Card("K", 13, "Club", Color.black, "ck.png"), new Card("K", 13, "Spade", Color.black, "sk.png"),
    new Card("Q", 12, "Heart", Color.red, "hq.png"), new Card("Q", 12, "Diamond", Color.red, "dq.png"), new Card("Q", 12, "Club", Color.black, "cq.png"), new Card("Q", 12, "Spade", Color.black, "sq.png"),
    new Card("J", 11, "Heart", Color.red, "hj.png"), new Card("J", 11, "Diamond", Color.red, "dj.png"), new Card("J", 11, "Club", Color.black, "cj.png"), new Card("J", 11, "Spade", Color.black, "sj.png"),
    new Card("10", 10, "Heart", Color.red, "h10.png"), new Card("10", 10, "Diamond", Color.red, "d10.png"), new Card("10", 10, "Club", Color.black, "c10.png"), new Card("10", 10, "Spade", Color.black, "s10.png"),
    new Card("9", 9, "Heart", Color.red, "h9.png"), new Card("9", 9, "Diamond", Color.red, "d9.png"), new Card("9", 9, "Club", Color.black, "c9.png"), new Card("9", 9, "Spade", Color.black, "s9.png"),
    new Card("8", 8, "Heart", Color.red, "h8.png"), new Card("8", 8, "Diamond", Color.red, "d8.png"), new Card("8", 8, "Club", Color.black, "c8.png"), new Card("8", 8, "Spade", Color.black, "s8.png"),
    new Card("7", 7, "Heart", Color.red, "h7.png"), new Card("7", 7, "Diamond", Color.red, "d7.png"), new Card("7", 7, "Club", Color.black, "c7.png"), new Card("7", 7, "Spade", Color.black, "s7.png")
  )
  private var currentDeck: List[Card] = List.empty
  def init = {
    // shuffle a new deck
    currentDeck = Random.shuffle(newDeck)
  }
  def length = { currentDeck.size }
  def deal: Card = {
    // deal the deck from the top, return the top card so it can be added to a hand
    //TODO: make deck a sequence
    val topCard = currentDeck.head
    currentDeck = currentDeck.tail
    topCard
  }
  def showTopCard: Card = { currentDeck.head }
}
