package com.github.bun133.testfly.dummy.server.utils

import org.bukkit.entity.Player
import java.util.UUID

class PlayerSpawnFaker(val utils: ServerUtils) {
    private val entityPlayerClazz = utils.classForNameUnderMinecraftServer("EntityPlayer")
    private val entityPlayerConstructor = utils.classForNameUnderMinecraftServer("EntityPlayer").constructors[0]

    fun fakePlayerSpawn(prof: FakePlayerProfile): Player {
        val en = generateEntity(prof)
        // TODO Send PlayerJoinEvent
        return entityPlayerClazz.getMethod("getBukkitEntity").invoke(en) as Player
    }

    /**
     * @return EntityPlayer
     */
    private fun generateEntity(prof: FakePlayerProfile): Any {
        val server = utils.getMinecraftServer()
        val worldServer = utils.vanillaServerClass
                .getMethod("getWorldServer", utils.overWorld().javaClass)
                .invoke(server, utils.overWorld())
        val profile = gameProfile(prof)
        val interactManager =
            utils.classForNameUnderMinecraftServer("PlayerInteractManager").constructors[0].newInstance(worldServer)
        val e = entityPlayerConstructor.newInstance(server, worldServer, profile, interactManager)
        entityPlayerClazz.getField("isRealPlayer").set(e, true)
        return e
    }

    fun gameProfile(fake: FakePlayerProfile): Any {
        return utils.classForName("com.mojang.authlib.GameProfile")
            .getConstructor(UUID::class.java, String::class.java)
            .newInstance(fake.uuid, fake.name)
    }

    class FakePlayerProfile(val name: String, val uuid: UUID)
}