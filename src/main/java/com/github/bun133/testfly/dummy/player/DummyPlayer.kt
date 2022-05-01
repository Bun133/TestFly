package com.github.bun133.testfly.dummy.player

import com.github.bun133.testfly.dummy.server.ConnectedServer
import org.bukkit.Location

class DummyPlayer(spawnLocation: Location,connectedServer: ConnectedServer) {
    init {
        spawnAt(spawnLocation,connectedServer)
    }

    companion object {
        private fun spawnAt(loc: Location, server: ConnectedServer) {
            loc.world.spawnEntity(loc, org.bukkit.entity.EntityType.PLAYER)
        }
    }
}