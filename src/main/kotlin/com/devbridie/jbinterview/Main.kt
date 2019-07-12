package com.devbridie.jbinterview

import com.devbridie.jbinterview.destructure.transformDestructure
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file

class Main : CliktCommand() {
    val input by argument("Input file").file(exists = true, folderOkay = false, readable = true)
    override fun run() {
        val ast = parseFile(input)
        val transformed = transformDestructure(ast)
        val result = transformed.toSource()
        echo(result)
    }
}

fun main(args: Array<String>) = Main().main(args)