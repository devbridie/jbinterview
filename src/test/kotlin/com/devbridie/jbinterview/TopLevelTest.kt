package com.devbridie.jbinterview

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class TestCase(val baseName: String) {
    val clz = this::class.java
    val fileStream = clz.getResourceAsStream("/js/$baseName")

    // If there is no .after, we expect no changes to have been made
    val expectedResult = runCatching {
        clz.getResourceAsStream("/js/$baseName.after").reader().readText()
    }.getOrElse {
        fileStream.reader().readText()
    }

    fun execute() {
        val ast = parseStream(fileStream)
        val transformed = transformDestructure(ast)
        val result = transformed.toSource()
        Assertions.assertEquals(expectedResult, result)
    }

}

class TopLevelTest {
    @Test
    fun testTopLevel() {
        TestCase("toplevel.js").execute()
    }
}
