package com.devbridie.jbinterview.destructure

import org.mozilla.javascript.ast.*

/**
 * Creates a [DestructureUsage] from a [VariableInitializer].
 * These have a structure of const/var/let <name> = <array>[<index>],
 * where `array` must be a [Name], and `index` must be a [NumberLiteral] that is an integer.
 */
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

    // we create groups that have the following similarities:
    // * Usages must refer to the same array
    // * Usages have same parent scope (we reduce complexity by not considering control flow)
    // * Declarations use the same keyword to declare variables (we do not modify program semantics)
    val groupingProperties = listOf(
        fromArray.definingScope,
        fromArray.identifier, // this pair is unique to each array variable declaration
        declaration.type, // group same type
        declaration.parent // group only in same scope
    )

    fun removeSelf() {
        declaration.variables.remove(this.initializer)
        if (declaration.variables.isEmpty()) {
            declaration.parent.removeChild(declaration)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DestructureUsage

        if (initializer != other.initializer) return false

        return true
    }

    override fun hashCode(): Int {
        return initializer.hashCode()
    }
}
