package com.github.bun133.testfly.dummy.server

import com.github.bun133.testfly.dummy.server.utils.ServerUtils
import org.bukkit.Server
import java.io.File

/**
 * 自前で[JARDummyServer]を動かすか、すでにサーバーが立ち上がっている場合はStatic関数からServerを取得する
 */
class ConnectedServer(val jarFile: File) : DummyServer {
    val isStandAlone = run {
        val clazz = getClassWithoutLoading("org.bukkit.Bukkit")
        if (clazz != null) {
            clazz.getDeclaredMethod("getServer")
                .invoke(null) == null
        } else {
            true
        }
    }

    var standAloneServer: JARDummyServer? = null

    private fun getClassWithoutLoading(name: String): Class<*>? {
        return try {
            Class.forName(name)
        } catch (e: ClassNotFoundException) {
            null
        }
    }

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

        utils = ServerUtils(this)
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

    lateinit var utils: ServerUtils
}