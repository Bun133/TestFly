package com.github.bun133.testfly.dummy.server

import org.bukkit.Server
import java.io.File
import java.lang.reflect.Method
import java.net.URLClassLoader

class JARDummyServer(private val jarFile: File) : DummyServer {
    lateinit var getServerMethod: Method
    lateinit var shutdownMethod: Method

    override fun getServer(): Server {
        return getServerMethod.invoke(null) as Server
    }

    override fun startServer(option: DummyServerOption) {
        if (!jarFile.exists()) {
            throw IllegalStateException("jar file not found")
        } else if (!jarFile.isFile) {
            throw IllegalStateException("jar file is not a file")
        }
        val urlClassLoader = URLClassLoader(arrayOf(jarFile.toURI().toURL()))
        val toLoadClazz = Class.forName("org.bukkit.craftbukkit.Main", true, urlClassLoader)
        val mainMethod = toLoadClazz.getDeclaredMethod("main", Array<String>::class.java)
        mainMethod.invoke(option.toStringParameter())   // Invoking Server.main(args)
        val serverClazz = Class.forName("org.bukkit.Bukkit", true, urlClassLoader)
        getServerMethod = serverClazz.getDeclaredMethod("getServer")
        shutdownMethod = serverClazz.getDeclaredMethod("shutdown")
    }

    override fun stopServer(isForce: Boolean) {
        shutdownMethod.invoke(null)
    }
}