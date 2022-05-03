package com.github.bun133.testfly.test

import com.github.bun133.testfly.dummy.server.ConnectedServer
import com.github.bun133.testfly.dummy.server.ConnectedServerOptions
import com.github.bun133.testfly.dummy.server.DummyServerOption
import com.github.bun133.testfly.dummy.server.utils.PlayerSpawnFaker
import java.io.File
import java.util.UUID

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
        server.startServer(DummyServerOption(isNoGui = true))
        println("[TEST] Server started successfully")
    }

    fun testAll() {
        checkServerPackage()
        getServerTest()
        fakePlayerJoin()
        waitForServerShutdown()
    }

    fun checkServerPackage() {
        assert(server.utils.serverClassPackagePath() == "net.minecraft.server.v1_16_R3")
    }

    fun getServerTest() {
        server.getServer()
    }

    fun fakePlayerJoin() {
        val e = PlayerSpawnFaker.FakePlayerProfile("Faker", UUID.randomUUID())

        val p = server.features.fakePlayerSpawn.fakePlayerSpawn(e)

        println("[TEST] Fake player joined, name: ${p.name}, uuid: ${p.uniqueId}")
    }

    fun waitForServerShutdown() {
        while (true) {
            Thread.sleep(1000)
            if (server.isDead()) break
        }
    }
}