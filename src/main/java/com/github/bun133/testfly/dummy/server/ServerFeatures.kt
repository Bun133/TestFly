package com.github.bun133.testfly.dummy.server

import com.github.bun133.testfly.dummy.server.utils.PlayerSpawnFaker
import com.github.bun133.testfly.dummy.server.utils.ServerUtils

class ServerFeatures(val utils: ServerUtils) {
    val fakePlayerSpawn = PlayerSpawnFaker(utils)
}
