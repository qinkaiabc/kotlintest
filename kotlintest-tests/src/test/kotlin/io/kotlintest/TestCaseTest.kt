package io.kotlintest

import io.kotlintest.runner.junit5.specs.StringSpec

class TestCaseTest : StringSpec() {

  object TagA : Tag()
  object TagB : Tag()

  init {
    val testTaggedA = "should be tagged with tagA" { }
    testTaggedA.config(tags = setOf(TagA))

    val untaggedTest = "should be untagged" { }

    val testTaggedB = "should be tagged with tagB" { }
    testTaggedB.config(tags = setOf(TagB))

    "only tests without excluded tags should be active" {
      System.setProperty("kotlintest.tags.exclude", "TagB")
      testTaggedA.isActive() shouldBe true
      untaggedTest.isActive() shouldBe true
      testTaggedB.isActive() shouldBe false
    }

    "only tests with included tags should be active" {
      System.setProperty("kotlintest.tags.include", "TagA")
      testTaggedA.isActive() shouldBe true
      untaggedTest.isActive() shouldBe false
      testTaggedB.isActive() shouldBe false
    }

    "tagged tests should be active by default" {
      testTaggedA.isActive() shouldBe true
      untaggedTest.isActive() shouldBe true
      testTaggedB.isActive() shouldBe true
    }
  }

  override fun interceptTestCase(testCase: TestCase, test: () -> Unit) {
    test()
    System.clearProperty("kotlintest.tags.exclude")
    System.clearProperty("kotlintest.tags.include")
  }
}