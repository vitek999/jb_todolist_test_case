package todo_list.utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UtilsExtensionsTest {

    @Test
    fun `string with one digit can convert to long`() {
        val string = "1"

        assertTrue(string.canConvertToLong())
    }

    @Test
    fun `string with more than one digit can convert to long`() {
        val string = "123"

        assertTrue(string.canConvertToLong())
    }

    @Test
    fun `empty string can't convert to long`() {
        val string = ""

        assertFalse(string.canConvertToLong())
    }

    @Test
    fun `blank string can't convert to long`() {
        val string = " "

        assertFalse(string.canConvertToLong())
    }

    @Test
    fun `string with negative digit can convert to long`() {
        val string = "-1"

        assertTrue(string.canConvertToLong())
    }

    @Test
    fun `string with letters can't convert to long`() {
        val string = "a"

        assertFalse(string.canConvertToLong())
    }
}