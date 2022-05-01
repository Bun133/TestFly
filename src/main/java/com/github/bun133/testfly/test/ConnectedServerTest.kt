package com.github.bun133.testfly.test

import com.github.bun133.testfly.dummy.server.ConnectedServer
import com.github.bun133.testfly.dummy.server.ConnectedServerOptions
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
    val server = ConnectedServer(ConnectedServerOptions(patchedServer, "v1_16_R3"))

    fun start() {
        server.startServer(DummyServerOption())
        println("[TEST] Server started successfully")
    }

    fun testAll() {
        checkServerPackage()
    }

    fun checkServerPackage() {
        assert(server.utils.serverClassPackagePath() == "net.minecraft.server.v1_16_R3")
    }
}