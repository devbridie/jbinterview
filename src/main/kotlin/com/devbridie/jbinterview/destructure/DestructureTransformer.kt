package com.devbridie.jbinterview.destructure

import org.mozilla.javascript.ast.*


fun findAllPossibleDestructureUsages(input: AstRoot): List<DestructureUsage> {
    return mutableListOf<DestructureUsage>().apply {
        input.visit { node ->
            if (node is VariableInitializer) {
                val dsu = DestructureUsage.fromVariableInitializer(node)
                if (dsu != null) {
                    add(dsu)
                }
            }
            true
        }
    }
}

// Walk backward on the AST to find the first [DestructureUsage] in this chain
// Chains are variable initializers (can be across declarations) that have the same groupingProperties
fun findFirstInChain(usage: DestructureUsage): DestructureUsage {
    val currentDeclaration = usage.declaration
    val initializerBefore = runCatching {
        val variables = currentDeclaration.variables
        val index = variables.indexOf(usage.initializer)
        if (index != 0) variables[index - 1]
        else null
    }.getOrNull()

    var destructureBefore = initializerBefore?.let { DestructureUsage.fromVariableInitializer(initializerBefore) }
    if (destructureBefore == null) {
        val declarationBefore =
            currentDeclaration.parent.getChildBefore(currentDeclaration) as? VariableDeclaration ?: return usage
        // then get the last initializer
        val lastInit = declarationBefore.variables.lastOrNull() ?: return usage
        destructureBefore = DestructureUsage.fromVariableInitializer(lastInit) ?: return usage
    }

    return if (destructureBefore.groupingProperties == usage.groupingProperties) {
        findFirstInChain(destructureBefore)
    } else {
        usage
    }
}

/**
 * We find all chains of possible usages of destructure, i.e. a series of uninterrupted [VariableInitializer]s
 * that all refer to the same target array.
 * Then we create a destructure variable declaration for each chain.
 */
fun transformDestructure(inputAst: AstRoot): AstRoot {
    findAllPossibleDestructureUsages(inputAst).groupBy { findFirstInChain(it) }.values.forEach { usages ->
        if (usages.size == 1) return@forEach
        val firstUsage = usages.first()
        val arrayLiteral = constructDestructureArray(usages)

        val destructureDeclaration = VariableDeclaration().apply {
            setIsStatement(true)
            type = firstUsage.declarationType
            addVariable(VariableInitializer().apply {
                target = arrayLiteral
                initializer = firstUsage.fromArray
            })
        }
        firstUsage.declaration.parent.addChildBefore(destructureDeclaration, firstUsage.declaration)
        usages.forEach {
            it.removeSelf()
        }
    }

    return inputAst
}

private fun constructDestructureArray(usages: List<DestructureUsage>) = ArrayLiteral().apply {
    val highestIndex = usages.map { it.index.number.toInt() }.max()!!
    elements = (0..highestIndex).map { index ->
        // find a destructure result with this index
        val match = usages.find { index == it.index.toInt() }
        if (match == null) {
            EmptyExpression()
        } else {
            Name().apply { identifier = match.intoName.identifier }
        }
    }
}


