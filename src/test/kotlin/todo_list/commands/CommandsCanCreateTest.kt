package todo_list.commands

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import todo_list.commands.exceptions.CommandFormatException

class CommandsCanCreateTest {

    @Test
    fun `can create print all tasks command without filters`() {
        val command = listOf("tasks")

        assertTrue(PrintAllTasksCommand.canCreate(command))
    }

    @Test
    fun `can create print all tasks command with active tasks filter`() {
        val command = listOf("tasks", "active")

        assertTrue(PrintAllTasksCommand.canCreate(command))
    }

    @Test
    fun `can create print all tasks command with done tasks filter`() {
        val command = listOf("tasks", "done")

        assertTrue(PrintAllTasksCommand.canCreate(command))
    }

    @Test
    fun `can create print all tasks command with incorrect filter`() {
        val command = listOf("tasks", "some")

        val exception = assertThrows<CommandFormatException> {
            PrintAllTasksCommand.canCreate(command)
        }
        assertEquals("Unknown param some for tasks command", exception.message)
    }

    @Test
    fun `can create print task by id command without id`() {
        val command = listOf("task")

        val exception = assertThrows<CommandFormatException> {
            PrintTaskByIdCommand.canCreate(command)
        }
        assertEquals("you should add id of task", exception.message)
    }

    @Test
    fun `can create print task by id command with correct id`() {
        val command = listOf("task", "1")
        assertTrue(PrintTaskByIdCommand.canCreate(command))
    }

    @Test
    fun `can create print task by id command with incorrect id`() {
        val command = listOf("task", "someText")

        val exception = assertThrows<CommandFormatException> {
            PrintTaskByIdCommand.canCreate(command)
        }
        assertEquals("someText is not digit", exception.message)
    }

    @Test
    fun `can create delete task without id`() {
        val command = listOf("delete")

        val exception = assertThrows<CommandFormatException> {
            DeleteTaskByIdCommand.canCreate(command)
        }
        assertEquals("you should add id of task", exception.message)
    }

    @Test
    fun `can create delete task with correct id`() {
        val command = listOf("delete", "1")

        assertTrue(DeleteTaskByIdCommand.canCreate(command))
    }

    @Test
    fun `can create delete task with incorrect id`() {
        val command = listOf("delete", "someText")

        val exception = assertThrows<CommandFormatException> {
            DeleteTaskByIdCommand.canCreate(command)
        }
        assertEquals("someText is not digit", exception.message)
    }

    @Test
    fun `can create delete-all tasks command`() {
        val command = listOf("delete-all")

        assertTrue(DeleteAllTasksCommand.canCreate(command))
    }

    @Test
    fun `can create complete task by id without id`() {
        val command = listOf("complete")

        val exception = assertThrows<CommandFormatException> {
            CompleteTaskByIdCommand.canCreate(command)
        }
        assertEquals("you should add id of task", exception.message)
    }

    @Test
    fun `can create complete task by id with incorrect id`() {
        val command = listOf("complete", "someText")

        val exception = assertThrows<CommandFormatException> {
            CompleteTaskByIdCommand.canCreate(command)
        }
        assertEquals("someText is not digit", exception.message)
    }

    @Test
    fun `can create complete task by id with correct id`() {
        val command = listOf("complete", "1")

        assertTrue(CompleteTaskByIdCommand.canCreate(command))
    }

    @Test
    fun `can create activate task by id without id`() {
        val command = listOf("activate")

        val exception = assertThrows<CommandFormatException> {
            ActivateTaskByIdCommand.canCreate(command)
        }
        assertEquals("you should add id of task", exception.message)
    }

    @Test
    fun `can create activate task by id with incorrect id`() {
        val command = listOf("activate", "someText")

        val exception = assertThrows<CommandFormatException> {
            ActivateTaskByIdCommand.canCreate(command)
        }
        assertEquals("someText is not digit", exception.message)
    }

    @Test
    fun `can create activate task by id with correct id`() {
        val command = listOf("activate", "1")

        assertTrue(ActivateTaskByIdCommand.canCreate(command))
    }

    @Test
    fun `create task command without name`() {
        val command = listOf("create")

        val exception = assertThrows<CommandFormatException> {
            CreateTaskCommand.canCreate(command)
        }
        assertEquals("task name not found", exception.message)
    }

    @Test
    fun `create task command with name`() {
        val command = listOf("create", "some", "task", "name")

        assertTrue(CreateTaskCommand.canCreate(command))
    }
}