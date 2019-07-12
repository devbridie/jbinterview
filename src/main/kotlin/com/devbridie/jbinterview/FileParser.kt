package com.devbridie.jbinterview

import org.mozilla.javascript.CompilerEnvirons
import org.mozilla.javascript.IRFactory
import java.io.File
import java.io.InputStream
import java.io.Reader

val defaultEnv = CompilerEnvirons().apply {
    setRecoverFromErrors(true)
    isGenerateDebugInfo = true
    isRecordingComments = true
}

fun createFactory() = IRFactory(defaultEnv)

fun parseFile(file: File) = parseReader(file.reader())
fun parseStream(stream: InputStream) = parseReader(stream.reader())

fun parseString(string: String) = createFactory().parse(string, null, 0)
fun parseReader(reader: Reader) = createFactory().parse(reader, null, 0)