package com.devbridie.jbinterview

import org.mozilla.javascript.CompilerEnvirons
import org.mozilla.javascript.IRFactory
import org.mozilla.javascript.ast.AstRoot
import java.io.File
import java.io.InputStream
import java.io.Reader

val defaultEnv = CompilerEnvirons().apply {
    setRecoverFromErrors(true)
    isGenerateDebugInfo = true
    isRecordingComments = true
}

fun parseFile(file: File) =parseReader(file.reader())

fun parseStream(stream: InputStream) = parseReader(stream.reader())

fun parseReader(reader: Reader): AstRoot {
    return IRFactory(defaultEnv).parse(reader, null, 0)
}