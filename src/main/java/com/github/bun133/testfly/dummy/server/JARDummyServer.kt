package com.github.bun133.testfly.dummy.server

import java.io.File
import java.lang.reflect.Method
import java.net.URLClassLoader

class JARDummyServer(private val jarFile: File) : DummyServer {
    lateinit var getServerMethod: Method
    lateinit var shutdownMethod: Method

    val urlClassLoader = URLClassLoader(arrayOf(jarFile.toURI().toURL()), this.javaClass.classLoader)

    override fun getServer(): org.bukkit.Server {
        // TODO Fix NoClassDefFoundError
        return getServerMethod.invoke(null) as org.bukkit.Server
    }

    override fun startServer(option: DummyServerOption) {
        if (!jarFile.exists()) {
            throw IllegalStateException("jar file not found")
        } else if (!jarFile.isFile) {
            throw IllegalStateException("jar file is not a file")
        }

        val toLoadClazz = Class.forName("org.bukkit.craftbukkit.Main", true, urlClassLoader)
        val mainMethod = toLoadClazz.getDeclaredMethod("main", Array<String>::class.java)
        mainMethod.invoke(null, arrayOf(option.toStringParameter()))
        val serverClazz = Class.forName("org.bukkit.Bukkit", true, urlClassLoader)
        getServerMethod = serverClazz.getDeclaredMethod("getServer")
        shutdownMethod = serverClazz.getDeclaredMethod("shutdown")

        Thread.sleep(1000)

        waitForServer()

        println("[TestFly]Server started")
    }

    private fun waitForServer() {
        while (true) {
            if (getServerMethod.invoke(null) != null) {
                break
            } else {
                Thread.sleep(10)
            }
        }
    }

    override fun stopServer(isForce: Boolean) {
        shutdownMethod.invoke(null)
    }
}