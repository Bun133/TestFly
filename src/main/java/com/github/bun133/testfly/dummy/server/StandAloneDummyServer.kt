package com.github.bun133.testfly.dummy.server

import com.github.bun133.testfly.dummy.server.utils.ServerUtils
import com.github.bun133.testfly.util.waitForSingle
import java.lang.reflect.Method

class StandAloneDummyServer(private val util: ServerUtils) : DummyServer {
    lateinit var getServerMethod: Method
    lateinit var shutdownMethod: Method


    override fun getServer(): org.bukkit.Server {
        return getServerMethod.invoke(null) as org.bukkit.Server
    }

    override fun startServer(option: DummyServerOption) {
        val toLoadClazz = util.classForName("org.bukkit.craftbukkit.Main")
        val mainMethod = toLoadClazz.getDeclaredMethod("main", Array<String>::class.java)
        mainMethod.invoke(null, arrayOf(option.toStringParameter()))
        val serverClazz = util.classForName("org.bukkit.Bukkit")
        getServerMethod = serverClazz.getDeclaredMethod("getServer")
        shutdownMethod = serverClazz.getDeclaredMethod("shutdown")

        Thread.sleep(1000)

        waitForServerStartUp()

        println("[TestFly]StandAlone Server started")
    }

    private fun waitForServerStartUp() {
        waitForSingle("Timings Reset", 10)
        println("OUT")
    }


    private var isStopped = false
    override fun stopServer(isForce: Boolean) {
        shutdownMethod.invoke(null)
        isStopped = true
    }

    override fun isDead(): Boolean {
        return isStopped
    }
}