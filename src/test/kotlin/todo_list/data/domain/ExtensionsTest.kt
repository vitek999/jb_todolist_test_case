package todo_list.data.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExtensionsTest {

    @Test
    fun `empty list to map`() {
        val list = emptyList<Task>()

        val result = list.toMap()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `list with one element`() {
        val taskId = 1L
        val task = Task("1", false, taskId)
        val list = listOf(task)
        val expectedMap = mapOf(taskId to task)

        val result = list.toMap()

        assertEquals(expectedMap, result)
    }

    @Test
    fun `list with more than element without repeats task id`() {
        val list = listOf(Task("1", false, 1L), Task("2", false, 2L))
        val expectedMap = mapOf(
            list[0].id to list[0],
            list[1].id to list[1]
        )

        val result = list.toMap()

        assertEquals(expectedMap, result)
    }

    @Test
    fun `list with more than element with repeats task id`() {
        val list = listOf(
            Task("1", false, 1L),
            Task("2", false, 2L),
            Task("3", false, 2L)
        )

        val expectedMap = mapOf(
            list[0].id to list[0],
            list[2].id to list[2]
        )

        val result = list.toMap()

        assertEquals(expectedMap, result)
    }
}
