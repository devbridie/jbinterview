package com.devbridie.jbinterview

import com.devbridie.jbinterview.destructure.transformDestructure
import org.junit.jupiter.api.Assertions

class TestCase(val path: String) {
    val clz = this::class.java
    fun getFileStream() = clz.getResourceAsStream(path) ?: error("Resource ${path} does not exist")

    // If there is no .after, we expect no changes to have been made
    val expectedResult = runCatching {
        clz.getResourceAsStream("$path.after").reader().readText()
    }.getOrElse {
        getFileStream().reader().readText()
    }

    fun execute() {
        val ast = parseStream(getFileStream())
        val transformed = transformDestructure(ast)
        val result = transformed.toSource()
        val expected = parseString(expectedResult).toSource()
        Assertions.assertEquals(expected.trim(), result.trim())
    }
}