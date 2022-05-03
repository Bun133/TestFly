package com.github.bun133.testfly.dummy.server

import com.github.bun133.testfly.dummy.server.utils.ServerUtils
import java.lang.reflect.Method
import org.bukkit.Server

class StandAloneDummyServer(private val util: ServerUtils) : DummyServer {
    lateinit var getServerMethod: Method
    lateinit var shutdownMethod: Method


    override fun getServer(): Server {
        return getServerMethod.invoke(null) as Server
    }

    override fun startServer(option: DummyServerOption) {
        val toLoadClazz = util.classForName("org.bukkit.craftbukkit.Main")
        val mainMethod = toLoadClazz.getDeclaredMethod("main", Array<String>::class.java)
        mainMethod.invoke(null, arrayOf(option.toStringParameter()))
        val serverClazz = util.classForName("org.bukkit.Bukkit")
        getServerMethod = serverClazz.getDeclaredMethod("getServer")
        shutdownMethod = serverClazz.getDeclaredMethod("shutdown")

        println("[TestFly]StandAlone Server started")
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