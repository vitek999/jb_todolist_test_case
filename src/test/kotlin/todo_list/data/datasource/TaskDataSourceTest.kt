package todo_list.data.datasource

import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import todo_list.data.Result
import todo_list.data.TEST_FILES_DIR
import todo_list.data.domain.Task
import todo_list.data.exceptions.TaskNotFoundException
import java.io.File

class TaskDataSourceTest {

    private val testFile = File("$TEST_FILES_DIR/test-data-source.json")
    private val dataSource: TasksDataSource = JsonFileTasksDataSource(testFile)
    private val json: Json = Json(JsonConfiguration.Stable.copy(prettyPrint = true))

    @BeforeEach
    fun beforeTest() {
        testFile.writeText("")
    }

    @Test
    fun `get empty tasks Success`() {
        // writing tasks to file
        testFile.writeText(json.stringify(Task.serializer().list, emptyList()))

        val result = dataSource.getTasks()

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.isEmpty())
    }

    @Test
    fun `get list of single tasks success`() {
        // writing tasks to file
        val task = Task("1", false, 1)
        testFile.writeText(json.stringify(Task.serializer().list, listOf(task)))

        val result = dataSource.getTasks()

        assertTrue(result is Result.Success)
        assertEquals(listOf(task), (result as Result.Success).data)
    }

    @Test
    fun `get list of more than one tasks success`() {
        // writing tasks to file
        val tasks = listOf(Task("1", false, 1), Task("1", false, 1))
        testFile.writeText(json.stringify(Task.serializer().list, tasks))

        val result = dataSource.getTasks()

        assertTrue(result is Result.Success)
        assertEquals(tasks, (result as Result.Success).data)
    }

    @Test
    fun `get task success by correct id`() {
        val taskId = 1L
        val task = Task("1", false, taskId)
        testFile.writeText(json.stringify(Task.serializer().list, listOf(task)))

        val result = dataSource.getTask(taskId)

        assertTrue(result is Result.Success)
        assertEquals(task, (result as Result.Success).data)
    }

    @Test
    fun `get task fail by wrong id`() {
        val taskId = 1L
        val task = Task("1", false, taskId)
        testFile.writeText(json.stringify(Task.serializer().list, listOf(task)))

        val result = dataSource.getTask(2L)
        val expectedError = TaskNotFoundException(2L)
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is TaskNotFoundException)
        assertEquals(expectedError.message, result.exception.message)
    }

    @Test
    fun `save task in empty list success`() {
        val task = Task("1")
        val taskId = 1L

        dataSource.saveTask(task)
        val savedTask = (dataSource.getTask(taskId) as Result.Success).data

        assertEquals(task.copy(id = taskId), savedTask)
    }

    @Test
    fun `save task in not empty tasks list success`() {
        // writing tasks to file
        val tasks = listOf(Task("1", false, 0), Task("2", false, 1))
        testFile.writeText(json.stringify(Task.serializer().list, tasks))

        val task = Task("3")
        val taskId = 2L

        dataSource.saveTask(task)

        val savedTask = (dataSource.getTask(taskId) as Result.Success).data

        assertEquals(task.copy(id = taskId), savedTask)
    }

    @Test
    fun `complete task success with correct Id of task`() {

        val taskId = 1L
        val task = Task("1", done = false, id = taskId)
        testFile.writeText(json.stringify(Task.serializer().list, listOf(task)))

        dataSource.completeTask(taskId, true)

        val taskFromDataSource = (dataSource.getTask(taskId) as Result.Success).data

        assertTrue(taskFromDataSource.done)
    }

    @Test
    fun `complete task fail with wrong id of task`() {
        val taskId = 1L
        val task = Task("1", done = false, id = taskId)
        testFile.writeText(json.stringify(Task.serializer().list, listOf(task)))

        assertThrows<TaskNotFoundException> { dataSource.completeTask(2L, true) }
    }

    @Test
    fun `delete task success with correct Id of task`() {
        // writing tasks to file
        val taskId = 1L
        val tasks = listOf(Task("1", false, 0), Task("2", false, 1))
        testFile.writeText(json.stringify(Task.serializer().list, tasks))

        dataSource.deleteTask(taskId)
        val expectedTasks = listOf(Task("1", false, 0))
        val tasksFromDataSource = (dataSource.getTasks() as Result.Success).data

        assertEquals(expectedTasks, tasksFromDataSource)
    }

    @Test
    fun `delete task fail with wrong id of task`() {
        // writing tasks to file
        val taskId = 100L
        val tasks = listOf(Task("1", false, 0), Task("2", false, 1))
        testFile.writeText(json.stringify(Task.serializer().list, tasks))

        assertThrows<TaskNotFoundException> { dataSource.deleteTask(taskId) }

        val tasksFromDataSource = (dataSource.getTasks() as Result.Success).data

        assertEquals(tasks, tasksFromDataSource)
    }

    @Test
    fun `delete all tasks`() {
        // writing tasks to file
        val tasks = listOf(Task("1", false, 0), Task("2", false, 1))
        testFile.writeText(json.stringify(Task.serializer().list, tasks))

        dataSource.deleteAllTasks()

        val tasksFromDataSource = (dataSource.getTasks() as Result.Success).data

        assertTrue(tasksFromDataSource.isEmpty())
    }
}