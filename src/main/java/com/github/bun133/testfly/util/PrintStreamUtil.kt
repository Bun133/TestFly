package com.github.bun133.testfly.util

import java.io.ByteArrayOutputStream
import java.io.PrintStream

private var overrideBAOStream: ByteArrayOutputStream? = null
private var overridePrintSystem: PrintStream? = null
private var overrideErrBAOStream: ByteArrayOutputStream? = null
private var overrideErrPrintSystem: PrintStream? = null
private val callBacks = mutableMapOf<String, () -> Unit>()

fun findFor(str: String, back: () -> Unit) {
    callBacks[str] = back
}

fun waitForSingle(str: String, waitTime: Long) {
    var toLoop = true
    findFor(str) {
        toLoop = false
    }
    while (toLoop) {
        Thread.sleep(waitTime)
        update()
    }

    println("Found $str")
}

fun update() {
    val o = getPrintStream()
    val s = o.toString()

    val eo = getErrPrintStream()
    val es = eo.toString()

    if (s.isEmpty() && es.isEmpty()) return
    val sp = s.split("\n")
    val ep = es.split("\n")
    (sp + ep).forEach { ss ->
        callBacks.toList().find { ss.indexOf(it.first) != -1 }?.let {
            it.second()
            callBacks.remove(it.first)
        }
    }

    o.reset()
}

fun getPrintStream(): ByteArrayOutputStream {
    if (overrideBAOStream != null) {
        if (System.out != overrideBAOStream) {
            System.setOut(overridePrintSystem!!)
        }
        return overrideBAOStream!!
    }
    val out = ByteArrayOutputStream()
    val p = PrintStream(out)
    System.setOut(p)
    overrideBAOStream = out
    overridePrintSystem = p
    return overrideBAOStream!!
}


fun getErrPrintStream(): ByteArrayOutputStream {
    if (overrideErrBAOStream != null) {
        if (System.err != overrideErrBAOStream) {
            System.setErr(overrideErrPrintSystem!!)
        }
        return overrideErrBAOStream!!
    }
    val out = ByteArrayOutputStream()
    val p = PrintStream(out)
    System.setErr(p)
    overrideErrBAOStream = out
    overrideErrPrintSystem = p
    return overrideErrBAOStream!!
}