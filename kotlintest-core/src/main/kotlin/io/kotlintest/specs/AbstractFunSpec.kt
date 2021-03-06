package io.kotlintest.specs

import io.kotlintest.AbstractSpec
import io.kotlintest.TestCase
import io.kotlintest.TestContext
import io.kotlintest.lineNumber

abstract class AbstractFunSpec(body: AbstractFunSpec.() -> Unit = {}) : AbstractSpec() {

  init {
    body()
  }

  fun test(name: String, test: TestContext.() -> Unit): TestCase {
    val tc = TestCase(name, name() + "/" + name, this@AbstractFunSpec, test, lineNumber(), defaultTestCaseConfig)
    rootScopes.add(tc)
    return tc
  }
}