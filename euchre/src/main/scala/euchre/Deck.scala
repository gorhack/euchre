/**
 * Created by kyle on 2/10/15.
 */
package euchre

import util.Random

class Deck {
  /*
   * Deck contains 32 cards
   */
  private val newDeck = List(
    new Card("A", "Heart"), new Card("A", "Diamond"), new Card("A", "Club"), new Card("A", "Spade"),
    new Card("K", "Heart"), new Card("K", "Diamond"), new Card("K", "Club"), new Card("K", "Spade"),
    new Card("Q", "Heart"), new Card("Q", "Diamond"), new Card("Q", "Club"), new Card("Q", "Spade"),
    new Card("J", "Heart"), new Card("J", "Diamond"), new Card("J", "Club"), new Card("J", "Spade"),
    new Card("10", "Heart"), new Card("10", "Diamond"), new Card("10", "Club"), new Card("10", "Spade"),
    new Card("9", "Heart"), new Card("9", "Diamond"), new Card("9", "Club"), new Card("9", "Spade"),
    new Card("8", "Heart"), new Card("8", "Diamond"), new Card("8", "Club"), new Card("8", "Spade"),
    new Card("7", "Heart"), new Card("7", "Diamond"), new Card("7", "Club"), new Card("7", "Spade")
  )
  var currentDeck: List[Card] = List.empty
  def init = {
    currentDeck = Random.shuffle(newDeck)
  }
  def length = { currentDeck.size }
  def deal: Card = {
    val topCard = currentDeck.head
    currentDeck = currentDeck.tail
    topCard
  }
  def showTopCard: Card = { currentDeck.head }
}
