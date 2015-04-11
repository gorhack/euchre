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
    new Card("A", 14, "Heart", Color.red), new Card("A", 14, "Diamond", Color.red), new Card("A", 14, "Club", Color.black), new Card("A", 14, "Spade", Color.black),
    new Card("K", 13, "Heart", Color.red), new Card("K", 13, "Diamond", Color.red), new Card("K", 13, "Club", Color.black), new Card("K", 13, "Spade", Color.black),
    new Card("Q", 12, "Heart", Color.red), new Card("Q", 12, "Diamond", Color.red), new Card("Q", 12, "Club", Color.black), new Card("Q", 12, "Spade", Color.black),
    new Card("J", 11, "Heart", Color.red), new Card("J", 11, "Diamond", Color.red), new Card("J", 11, "Club", Color.black), new Card("J", 11, "Spade", Color.black),
    new Card("10", 10, "Heart", Color.red), new Card("10", 10, "Diamond", Color.red), new Card("10", 10, "Club", Color.black), new Card("10", 10, "Spade", Color.black),
    new Card("9", 9, "Heart", Color.red), new Card("9", 9, "Diamond", Color.red), new Card("9", 9, "Club", Color.black), new Card("9", 9, "Spade", Color.black),
    new Card("8", 8, "Heart", Color.red), new Card("8", 8, "Diamond", Color.red), new Card("8", 8, "Club", Color.black), new Card("8", 8, "Spade", Color.black),
    new Card("7", 7, "Heart", Color.red), new Card("7", 7, "Diamond", Color.red), new Card("7", 7, "Club", Color.black), new Card("7", 7, "Spade", Color.black)
  )
  var currentDeck: List[Card] = List.empty
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
