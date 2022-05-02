package com.github.bun133.testfly.dummy.server

data class DummyServerOption(
    val isNoGui: Boolean = false
) {
    fun toStringParameter(): String {
        return if (isNoGui) "--nogui" else ""
    }
}