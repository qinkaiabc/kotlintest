package io.kotlintest

import java.io.Closeable
import java.util.*

abstract class AbstractSpec : Spec {

  override fun isInstancePerTest(): Boolean = false

  internal val rootScopes = mutableListOf<TestScope>()

  override fun root(): TestContainer = TestContainer(name(), name(), this, { context ->
    rootScopes.forEach {
      context.addScope(it)
    }
  }, true)

  private val closeablesInReverseOrder = LinkedList<Closeable>()

  /**
   * Registers a field for auto closing after all tests have run.
   */
  protected fun <T : Closeable> autoClose(closeable: T): T {
    closeablesInReverseOrder.addFirst(closeable)
    return closeable
  }

  internal fun closeResources() {
    closeablesInReverseOrder.forEach { it.close() }
  }

  /**
   * Config applied to each test case if not overridden per test case.
   */
  protected open val defaultTestCaseConfig: TestCaseConfig = TestCaseConfig()
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DisplayName(val name: String)