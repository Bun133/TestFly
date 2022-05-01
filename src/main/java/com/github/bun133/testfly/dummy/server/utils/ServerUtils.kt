package com.github.bun133.testfly.dummy.server.utils

import com.github.bun133.testfly.dummy.server.ConnectedServer

class ServerUtils(val server: ConnectedServer) {
    val fakePlayerSpawn = PlayerSpawnFaker(this)

    fun getClassLoader(): ClassLoader {
        return if (server.isStandAlone) {
            server.standAloneServer!!.urlClassLoader
        } else {
            server.javaClass.classLoader
        }
    }

    fun classForName(name: String): Class<*> {
        return Class.forName(name, true, getClassLoader())
    }

    /**
     * This returns the package name of the server class.
     * e.g. "net.minecraft.server.v1_16_R3"
     */
    fun serverClassPackagePath(): String {
        return server.getServer().javaClass.`package`.name
    }
}