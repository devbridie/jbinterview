package com.devbridie.jbinterview

fun discoverTestCases(prefix: String = "/js/"): List<TestCase> {
    val clz = object {}::class.java
    val resources = clz.getResourceAsStream(prefix).reader().readLines()
    val testNames = resources.filter { it.endsWith(".js") }
    val folders = resources.filterNot { it.contains(".") }
    return testNames.map { TestCase(prefix + it) } + folders.flatMap { discoverTestCases(prefix + it + "/") }
}