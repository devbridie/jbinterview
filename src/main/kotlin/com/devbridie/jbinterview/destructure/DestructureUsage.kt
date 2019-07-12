package com.devbridie.jbinterview.destructure

import org.mozilla.javascript.ast.*

class DestructureUsage(
    val initializer: VariableInitializer
) {
    companion object {
        fun fromVariableInitializer(variableInitializer: VariableInitializer) = kotlin.runCatching {
            DestructureUsage(variableInitializer)
        }.getOrNull()
    }

    val intoName = initializer.target as Name
    val fromArray = (initializer.initializer as ElementGet).target as Name
    val index = (initializer.initializer as ElementGet).element as NumberLiteral
    val declaration = initializer.parent as VariableDeclaration
    val declarationType = declaration.type
    override fun toString(): String {
        return "DestructureUsage(initializer=${initializer.toSource()}, intoName=${intoName.toSource()}, fromArray=${fromArray.toSource()}, index=${index.toSource()})"
    }


    fun removeSelf() {
        // workaround
        if (declaration.firstChild == declaration.lastChild) {
            declaration.removeChildren()
        } else {
            declaration.removeChild(initializer)
        }
        if (!declaration.hasChildren()) {
            declaration.parent.removeChild(declaration)
        }
    }

}
