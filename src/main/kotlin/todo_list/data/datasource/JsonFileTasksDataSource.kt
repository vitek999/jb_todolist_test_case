package todo_list.data.datasource

import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import todo_list.data.Result
import todo_list.data.domain.Task
import todo_list.data.domain.toMap
import todo_list.data.exceptions.TaskNotFoundException
import java.io.File
import java.lang.IllegalArgumentException

/**
 * Datasource class for working with json file [jsonFile]
 *
 * @property jsonFile json file for store tasks
 * @property json [Json] entry point to work with JSON serialization.
 */
class JsonFileTasksDataSource(
    private val jsonFile: File,
    private val json: Json = Json(JsonConfiguration.Stable.copy(prettyPrint = true))
) : TasksDataSource {

    /**
     * Get all tasks from [jsonFile]
     *
     * @return list of tasks from [jsonFile]
     */
    override fun getTasks(): Result<List<Task>> {
        return try {
            Result.Success(readTasksFromFile())
        } catch (e: IllegalArgumentException) {
            Result.Error(e)
        }
    }

    /**
     * Get task by id from [jsonFile]
     *
     * @param taskId id of task
     *
     * @return [Result.Success] if [jsonFile] contains id with [taskId] or [Result.Error] with [TaskNotFoundException] if task not found
     */
    override fun getTask(taskId: Long): Result<Task> {
        return try {
            Result.Success(readTasksFromFile().first { it.id == taskId })
        } catch (e: NoSuchElementException) {
            Result.Error(TaskNotFoundException(taskId))
        }
    }

    /**
     * Save task into [jsonFile]
     *
     * When task saving id of task set on one more than max id from [jsonFile]
     *
     * @param task task for save in [jsonFile]
     */
    override fun saveTask(task: Task) {
        val id = readTasksFromFile().maxBy { it.id }?.id ?: 0
        val tmpTasks =  readTasksFromFile() + task.copy(id = id + 1)
        writeTasksToFile(tmpTasks)
    }

    /**
     * Set complete status of task with [taskId] in [jsonFile]
     *
     * @param taskId id of task
     * @param value new complete status of task with [taskId]
     * @throws TaskNotFoundException when [jsonFile] not contains task with [taskId]
     */
    override fun completeTask(taskId: Long, value: Boolean) {
        val tasksMap = readTasksFromFile().toMap().toMutableMap()
        if (tasksMap.containsKey(taskId)) {
            tasksMap[taskId] = tasksMap[taskId]!!.copy(done = value)
            writeTasksToFile(tasksMap.map { it.value }.toList())
        } else {
            throw TaskNotFoundException(taskId)
        }
    }

    /**
     * Delete task from [jsonFile] by [taskId]
     *
     * @param taskId id of task
     * @throws TaskNotFoundException when [jsonFile] not contains task with [taskId]
     */
    override fun deleteTask(taskId: Long) {
        val tmpList = readTasksFromFile().toMutableList()
        val result = tmpList.removeIf { it.id == taskId }
        if(!result) throw TaskNotFoundException(taskId)
        writeTasksToFile(tmpList)
    }

    /**
     * Delete all tasks from [jsonFile]
     */
    override fun deleteAllTasks() {
        clearFile()
    }

    /**
     * Read all tasks from [jsonFile]
     *
     * @return list of tasks from [jsonFile]
     * @throws IllegalArgumentException when file has wrong format
     */
    private fun readTasksFromFile(): List<Task>  {
        val jsonText = jsonFile.readLines().joinToString(separator = "")
        try {
            return if (jsonText.isNotEmpty()) {
                json.parse(Task.serializer().list, jsonText)
            } else {
                return emptyList<Task>()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Wrong file Format")
        }
    }

    /**
     * Write tasks in [jsonFile]
     *
     * @param tasks list of tasks for writing
     */
    private fun writeTasksToFile(tasks: List<Task>) {
        val tasksJson = json.stringify(Task.serializer().list, tasks)
        jsonFile.writeText(tasksJson)
    }

    /**
     * Delete all from [jsonFile]
     */
    private fun clearFile() {
        jsonFile.writeText("")
    }
}