package euchre

import scala.util.Random

/**
 * Created by kyle on 2/11/15.
 */
class Schema() {
// Schemas:
//  1. Aggressive: Always play highest card in hand if possible to win trick
//  2. Passive: Play lowest card to allow teammate to win tricks - play highest cards last
//  3. Semi-Aggressive: Always play highest card in hand unless partner is winning the trick
//  4. Aggressive-Lead: When partner leads, always play highest card if possible to increase
//     chance of taking trick. Always lead with highest cards.
//  5. Passive-Lead: Always lead with low cards to allow partner to take tricks.
//  6. Aggressive-Trump: Always trump to take trick when possible
//  7. Passive-Fail: Always fail off to allow teammate to take trick.

// Alternate easier schema:
// 1. (COMPLETE) Play highest card available. Trump with highest trump when available.
// 2. Play highest card available. Trump with lowest trump when available.
// 3. Play lowest card available. Trump with lowest trump when available.
// 4. Play lowest card available. Trump with highest trump when available.
// 5. Play highest card available. Do not trump until the end.
// 6. Play lowest card available. Do not trump until the end.

  def schemas = List("Aggressive", "Passive ", "Semi-Aggressive",
                    "Aggressive-Lead", "Passive-Lead", "Aggressive-Trump",
                    "Passive-Fail")
  private var _schema = ""
  def init = {
    // Randomly assign a schema
    _schema = Random.shuffle(schemas).head
  }
  def schema = _schema
  def schema_=(s:String): Unit = { _schema = s }
}
