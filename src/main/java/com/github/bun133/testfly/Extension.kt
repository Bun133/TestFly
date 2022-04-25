package com.github.bun133.testfly

import com.github.bun133.testfly.assertion.AssertSetting
import com.github.bun133.testfly.assertion.Assertion
import com.github.bun133.testfly.assertion.AssertionManager
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.PrintStream

private val assertionManagers = mutableMapOf<String, AssertionManager>()

fun JavaPlugin.getAssertionManager(setting: AssertSetting = AssertSetting()): AssertionManager {
    return assertionManagers.getOrPut(this.name) {
        AssertionManager(setting)
    }
}

fun <T> JavaPlugin.assert(name: String, expected: T, isOverWriteResult: Boolean = false): Assertion<T> {
    return Assertion(name, expected, isOverWriteResult, getAssertionManager())
}

fun JavaPlugin.reportToString(stream: PrintStream) {
    getAssertionManager().reportToString().forEach {
        stream.println(it)
    }
}

fun JavaPlugin.reportToString(sender: CommandSender) {
    getAssertionManager().reportToString().forEach {
        sender.sendMessage(it)
    }
}

fun JavaPlugin.report(): Pair<Boolean, List<String>> {
    return getAssertionManager().report()
}