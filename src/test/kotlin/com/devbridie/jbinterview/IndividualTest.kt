package com.devbridie.jbinterview

import org.junit.jupiter.api.Test

class IndividualTest {
    @Test
    fun testTopLevel() {
        TestCase("/js/toplevel.js").execute()
    }

    @Test
    fun testSameName() {
        TestCase("/js/scopes/sameName.js").execute()
    }
}
