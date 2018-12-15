package aph.rpgc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HelloWorldTests {

    @Test
    fun `Verify greeting`() {
        assertEquals("Hello, World!", GREETING)
    }
}
