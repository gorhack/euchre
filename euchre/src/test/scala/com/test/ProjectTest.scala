package com.test

import org.scalatest.{Matchers, FunSuite}

abstract class UnitSpec extends FunSuite with Matchers

class ProjectTester extends UnitSpec {

  test("An empty Set should have size 0") {
    assert(Set.empty.size === 0)
  }

  test("Invoking head on an empty Set should produce NoSuchElementException") {
    intercept[NoSuchElementException] {
      Set.empty.head
    }
  }
}