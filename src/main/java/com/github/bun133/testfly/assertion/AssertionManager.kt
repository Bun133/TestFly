package com.github.bun133.testfly.assertion

class AssertionManager(val setting: AssertSetting = AssertSetting()) {
    private val assertions = mutableListOf<Assertion<*>>()
    fun add(assertion: Assertion<*>) {
        assertions.add(assertion)
    }

    fun reportToString(): List<String> {
        val sb = StringBuilder()

        sb.append("===Assertion Report===\n")
        sb.append("Assertion Manager: $this\n")
        sb.append("Assertion Count: ${assertions.size}\n")
        sb.append("\n")

        for (assertion in assertions) {
            sb.append(assertion.reportToString())
            sb.append("\n")
        }

        return sb.toString().split("\n")
    }

    fun result(): Boolean {
        val isContainNotRun = assertions.any { it.state == AssertionState.NotRun }
        val isContainFailed = assertions.any { it.state == AssertionState.Failed }
        return (setting.isAllowNotRun || !isContainNotRun) && (setting.isAllowFailed || !isContainFailed)
    }

    fun report() = result() to reportToString()
}