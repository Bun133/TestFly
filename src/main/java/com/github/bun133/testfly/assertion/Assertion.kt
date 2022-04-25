package com.github.bun133.testfly.assertion

class Assertion<T>(val name: String, var expected: T, manager: AssertionManager) {
    init {
        manager.add(this)
    }

    var state: AssertionState = AssertionState.NotRun
        private set

    var lastValue: T? = null
        private set

    fun assert(f: () -> T) {
        val actual = f()
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