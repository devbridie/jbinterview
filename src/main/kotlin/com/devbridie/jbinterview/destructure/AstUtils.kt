package com.devbridie.jbinterview.destructure

import com.devbridie.jbinterview.parseString
import org.mozilla.javascript.ast.AstRoot
import org.mozilla.javascript.ast.NumberLiteral

fun AstRoot.copy() = parseString(toSource())

fun NumberLiteral.toInt() = this.number.toInt()