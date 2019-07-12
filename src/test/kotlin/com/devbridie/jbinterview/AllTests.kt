package com.devbridie.jbinterview

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class AllTests {
    @TestFactory
    fun testAll(): List<DynamicTest> {
        return discoverTestCases().map {
            DynamicTest.dynamicTest(it.path) {
                println("Testing ${it.path}")
                it.execute()
            }
        }
    }
}