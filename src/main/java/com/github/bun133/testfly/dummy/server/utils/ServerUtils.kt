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

    /**
     * e.g. "net.minecraft.server.v1_16_R3.MinecraftServer"
     */
    val vanillaServerClass by lazy { classForNameUnderMinecraftServer("MinecraftServer") }

    /**
     * Static Server Getter
     */
    private val serverGetter by lazy {
        classForNameUnderMinecraftServer("MinecraftServer").getDeclaredField("SERVER").also { it.isAccessible = true }
    }

    /**
     * returns the vanilla server object
     */
    fun getMinecraftServer(): Any? {
        return serverGetter.get(null)
    }

    /**
     * World Class
     */
    val vanillaWorldClass by lazy { classForNameUnderMinecraftServer("World") }

    private val worldOVERWORLDGetter by lazy { classForNameUnderMinecraftServer("World").getDeclaredField("OVERWORLD") }
    fun overWorld() = worldOVERWORLDGetter.get(null)
    private val worldTHE_NETHERGetter by lazy { classForNameUnderMinecraftServer("World").getDeclaredField("THE_NETHER") }
    fun theNether() = worldTHE_NETHERGetter.get(null)
    private val worldTHE_ENDGetter by lazy { classForNameUnderMinecraftServer("World").getDeclaredField("THE_END") }
    fun theEnd() = worldTHE_ENDGetter.get(null)

    fun serverThread() = Thread.getAllStackTraces().toList().find { it.first.name == "Server thread" }?.first
}