package com.devbridie.jbinterview.destructure

import org.mozilla.javascript.ast.*


fun transformDestructure(input: AstRoot): AstRoot {
    // we recursively find instances of const/var/let <name> = <array>[<index>];
    val initializers = mutableListOf<DestructureUsage>()
    input.visit { node ->
        if (node is VariableInitializer) {
            val dsu = DestructureUsage.fromVariableInitializer(node)
            if (dsu != null) {
                initializers.add(dsu)
            }
        }
        true
    }

    val map = initializers.groupBy { it.fromArray.identifier to it.declarationType }
    map.forEach { (name, declType), list ->
        if (list.size == 1) return@forEach
        val firstUsage = list.first()
        val range = 0..list.map { it.index.number.toInt() }.max()!!
        val addDestructure = VariableDeclaration().apply {
            setIsStatement(true)
            type = firstUsage.declarationType
            addVariable(VariableInitializer().apply {
                target = ArrayLiteral().apply {
                    range.forEach { index ->
                        val match = list.find { index == it.index.toInt() };
                        if (match == null) {
                            addElement(EmptyExpression())
                        } else {
                            addElement(Name().apply { identifier = match.intoName.identifier })
                        }
                    }
                }
                initializer = firstUsage.fromArray

            })
        }
        firstUsage.declaration.parent.addChildBefore(addDestructure, firstUsage.declaration)
        list.forEach {
            it.removeSelf()
        }
    }

    return input
}

