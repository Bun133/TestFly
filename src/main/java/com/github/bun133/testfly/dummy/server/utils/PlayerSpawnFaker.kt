package com.github.bun133.testfly.dummy.server.utils

import java.util.UUID

class PlayerSpawnFaker(val utils: ServerUtils) {
    private val entityPlayerClazz = utils.classForName("net.minecraft.server.v1_16_R3.EntityPlayer")

    fun fakePlayerSpawn(prof: FakePlayerProfile) {
        val en = generateEntity(prof)
    }

    private fun generateEntity(prof: FakePlayerProfile) {

    }

    class FakePlayerProfile(val name: String, val uuid: UUID)
}