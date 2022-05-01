package com.github.bun133.testfly.test

import com.github.bun133.testfly.dummy.server.ConnectedServer
import com.github.bun133.testfly.dummy.server.DummyServerOption
import java.io.File

fun main() {
    ConnectedServerTest().also {
        it.start()
        it.testAll()
    }
}

class ConnectedServerTest {
    val patchedServer = File("patched_1.16.5.jar")
    val server = ConnectedServer(patchedServer)

    fun start() {
        server.startServer(DummyServerOption())
    }

    fun testAll() {
        checkServerPackage()
    }

    fun checkServerPackage() {
        assert(server.utils.serverClassPackagePath() == "net.minecraft.server.v1_16_R3")
    }
}