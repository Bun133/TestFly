package com.github.bun133.testfly.assertion

/**
 * @property isOverWriteResult falseならば、結果を上書きせず、失敗したらそのまま失敗した状態を保つ
 */
class Assertion<T>(val name: String, var expected: T, val isOverWriteResult: Boolean, manager: AssertionManager) {
    init {
        manager.add(this)
    }

    var state: AssertionState = AssertionState.NotRun
        private set

    var lastValue: T? = null
        private set

    fun assert(f: () -> T) {
        if (isOverWriteResult) {
            applyState(f())
        } else {
            if (state != AssertionState.Failed) {
                applyState(f())
            }
        }
    }

    private fun applyState(actual: T) {
        lastValue = actual
        state = if (actual == expected) {
            AssertionState.Passed
        } else {
            AssertionState.Failed
        }
    }

    fun reportToString(): String {
        if (state == AssertionState.NotRun) {
            return "$name: Not Ran"
        }
        return "$name: ${state.name},Expected: $expected,Actual: $lastValue"
    }
}