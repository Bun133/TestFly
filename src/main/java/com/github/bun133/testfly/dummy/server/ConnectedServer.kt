package com.github.bun133.testfly.dummy.server

import com.github.bun133.testfly.dummy.server.utils.ServerUtils
import org.bukkit.Server
import java.net.URLClassLoader

/**
 * 自前で[StandAloneDummyServer]を動かすか、すでにサーバーが立ち上がっている場合はStatic関数からServerを取得する
 */
class ConnectedServer(val settings: ConnectedServerOptions) : DummyServer {
    val utils: ServerUtils = ServerUtils(this)

    val urlClassLoader = run {
        val urls = settings.jarFile.toURI().toURL()
        URLClassLoader(arrayOf(urls), this::class.java.classLoader)
    }

    init {
//        loadAllBukkitClasses()
    }

    val isStandAlone = run {
        val clazz = getClassNullable("org.bukkit.Bukkit")
        if (clazz != null) {
            clazz.getDeclaredMethod("getServer")
                .invoke(null) == null
        } else {
            true
        }
    }

    var standAloneServer: StandAloneDummyServer? = null

    private fun getClassNullable(name: String): Class<*>? {
        return try {
            Class.forName(name, true, urlClassLoader)
        } catch (e: ClassNotFoundException) {
            null
        }
    }

    private fun loadAllBukkitClasses() {
//        tryPreloadClass("com.destroystokyo.paper.util.SneakyThrow")
//        tryPreloadClass("com.google.common.collect.Iterators\$PeekingImpl")
//        tryPreloadClass("com.google.common.collect.MapMakerInternalMap\$Values")
//        tryPreloadClass("com.google.common.collect.MapMakerInternalMap\$ValueIterator")
//        tryPreloadClass("com.google.common.collect.MapMakerInternalMap\$WriteThroughEntry")
//        tryPreloadClass("com.google.common.collect.Iterables")

        for (i in 1..15) {
            tryPreloadClass("com.google.common.collect.Iterables$$i")
        }

        tryPreloadClass("org.bukkit.craftbukkit.libs.org.apache.commons.lang3.mutable.MutableBoolean")
        tryPreloadClass("org.bukkit.craftbukkit.libs.org.apache.commons.lang3.mutable.MutableInt")
        tryPreloadClass("org.jline.terminal.impl.MouseSupport")
        tryPreloadClass("org.jline.terminal.impl.MouseSupport$1")
        tryPreloadClass("org.jline.terminal.Terminal\$MouseTracking")
//        tryPreloadClass("co.aikar.timings.TimingHistory")
//        tryPreloadClass("co.aikar.timings.TimingHistory\$MinuteReport")
        tryPreloadClass("io.netty.channel.AbstractChannelHandlerContext")
        tryPreloadClass("io.netty.channel.AbstractChannelHandlerContext$11")
        tryPreloadClass("io.netty.channel.AbstractChannelHandlerContext$12")
        tryPreloadClass("io.netty.channel.AbstractChannel\$AbstractUnsafe$8")
        tryPreloadClass("io.netty.util.concurrent.DefaultPromise")
        tryPreloadClass("io.netty.util.concurrent.DefaultPromise$1")
        tryPreloadClass("io.netty.util.internal.PromiseNotificationUtil")
        tryPreloadClass("io.netty.util.internal.SystemPropertyUtil")
        tryPreloadClass("org.bukkit.craftbukkit.v1_16_R3.scheduler.CraftScheduler")
        tryPreloadClass("org.bukkit.craftbukkit.v1_16_R3.scheduler.CraftScheduler$1")
        tryPreloadClass("org.bukkit.craftbukkit.v1_16_R3.scheduler.CraftScheduler$2")
        tryPreloadClass("org.bukkit.craftbukkit.v1_16_R3.scheduler.CraftScheduler$3")
        tryPreloadClass("org.bukkit.craftbukkit.v1_16_R3.scheduler.CraftScheduler$4")
        tryPreloadClass("org.slf4j.helpers.MessageFormatter")
        tryPreloadClass("org.slf4j.helpers.FormattingTuple")
        tryPreloadClass("org.slf4j.helpers.BasicMarker")
        tryPreloadClass("org.slf4j.helpers.Util")
//        tryPreloadClass("com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent")
//        tryPreloadClass("com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent")
        tryPreloadClass("net.minecraft.server.v1_16_R3.LightEngineLayerEventListener")
        tryPreloadClass("net.minecraft.server.v1_16_R3.LightEngineLayerEventListener.Void")
        tryPreloadClass("net.minecraft.server.v1_16_R3.ExceptionSuppressor")
    }

    private fun tryPreloadClass(name: String) {
        try {
            utils.classForName(name)
        } catch (e: ClassNotFoundException) {
            println("[TestFly] Failed to load class: $name")
        }
    }

    override fun getServer(): Server {
        return if (isStandAlone) {
            standAloneServer!!.getServer()
        } else {
            utils.classForNameUnderBukkit("Bukkit")
                .getDeclaredMethod("getServer")
                .invoke(null) as Server
        }
    }

    override fun startServer(option: DummyServerOption) {
        if (isStandAlone) {
            standAloneServer = StandAloneDummyServer(utils)
            standAloneServer!!.startServer(option)
        }

        waitForServerStartUp(1000, 10)

        features = ServerFeatures(utils)
    }

    private fun waitForServerStartUp(firstWaitTime: Long, waitTime: Long) {
        val server = utils.getMinecraftServer()
        val tpsArr = utils.classForNameUnderMinecraftServer("MinecraftServer").getDeclaredField("recentTps")

        Thread.sleep(firstWaitTime)

        while (true) {
            val arr = tpsArr.get(server) as DoubleArray
            if (arr.any { it > 0.0 }) {
                break
            }
            Thread.sleep(waitTime)
        }

        println("[TestFly] Server started")
    }

    override fun stopServer(isForce: Boolean) {
        if (isStandAlone) {
            standAloneServer!!.stopServer(isForce)
        } else {
            utils.classForNameUnderBukkit("Bukkit")
                .getDeclaredMethod("shutdown")
                .invoke(null)
        }
    }

    override fun isDead(): Boolean {
        return Thread.getAllStackTraces().toList().find { it.first.name == "Server thread" } == null
    }

    lateinit var features: ServerFeatures
}