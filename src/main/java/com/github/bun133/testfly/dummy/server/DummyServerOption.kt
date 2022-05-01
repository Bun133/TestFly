package com.github.bun133.testfly.dummy.server

data class DummyServerOption(
    val port: Int = 8080,
    val isNoGui: Boolean = false
) {
    fun toStringParameter(): String {
        return "-port $port ${if (isNoGui) "-nogui" else ""}"
    }
}