package com.github.bun133.testfly.dummy.server.utils

import com.github.bun133.testfly.dummy.server.ConnectedServer

class ServerUtils(val server: ConnectedServer) {
    fun getClassLoader(): ClassLoader {
        return server.urlClassLoader
    }

    fun classForName(name: String): Class<*> {
        return Class.forName(name, true, getClassLoader())
    }

    fun classForNameUnderMinecraftServer(name: String): Class<*> {
        return classForName("${serverClassPackagePath()}.${name}")
    }

    fun classForNameUnderBukkit(name: String): Class<*> {
        return classForName("org.bukkit.${name}")
    }

    /**
     * This returns the package name of the server class.
     * e.g. "net.minecraft.server.v1_16_R3"
     */
    fun serverClassPackagePath(): String {
        return "net.minecraft.server." + server.settings.minecraftVersion
    }
}