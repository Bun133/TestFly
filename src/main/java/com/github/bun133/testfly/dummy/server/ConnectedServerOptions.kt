package com.github.bun133.testfly.dummy.server

import java.io.File


data class ConnectedServerOptions(
    val jarFile: File,
    // 内部のパッケージ名
    // e.g. v1_16_R3
    val minecraftVersion: String,
)