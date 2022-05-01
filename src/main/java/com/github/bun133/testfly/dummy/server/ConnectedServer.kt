package com.github.bun133.testfly.dummy.server

import org.bukkit.Server
import java.io.File

/**
 * 自前で[JARDummyServer]を動かすか、すでにサーバーが立ち上がっている場合はStatic関数からServerを取得する
 */
class ConnectedServer(val jarFile: File) : DummyServer {
    val isStandAlone = Class.forName("org.bukkit.Bukkit")
        .getDeclaredMethod("getServer")
        .invoke(null) == null

    var standAloneServer: JARDummyServer? = null

    override fun getServer(): Server {
        return if (isStandAlone) {
            standAloneServer!!.getServer()
        } else {
            Class.forName("org.bukkit.Bukkit")
                .getDeclaredMethod("getServer")
                .invoke(null) as Server
        }
    }

    override fun startServer(option: DummyServerOption) {
        if (isStandAlone) {
            standAloneServer = JARDummyServer(jarFile)
            standAloneServer!!.startServer(option)
        }
    }

    override fun stopServer(isForce: Boolean) {
        if (isStandAlone) {
            standAloneServer!!.stopServer(isForce)
        } else {
            Class.forName("org.bukkit.Bukkit")
                .getDeclaredMethod("shutdown")
                .invoke(null)
        }
    }
}