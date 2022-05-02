package com.github.bun133.testfly.dummy.server

import org.bukkit.Server

interface DummyServer {
    fun getServer(): Server
    fun startServer(option: DummyServerOption)
    fun stopServer(isForce: Boolean)

    /**
     * return true when server is not running
     */
    fun isDead(): Boolean
}