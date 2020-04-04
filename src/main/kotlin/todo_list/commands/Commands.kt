package todo_list.commands

import todo_list.commands.exceptions.CommandFormatException
import todo_list.data.Result
import todo_list.data.datasource.TasksDataSource
import todo_list.data.domain.Task
import todo_list.utils.canConvertToLong

/**
 * Abstract class of command for working with datasource and print result in console
 *
 * @property [dataSource] datasource for tasks
 */
abstract class Command(protected val dataSource: TasksDataSource) {

    /**
     * Execute command method
     */
    abstract fun run()

    /**
     * Execute when [run] execution is success
     */
    abstract fun onSuccess()

    /**
     * Execute when [run] execution is fail
     */
    abstract fun onFail(e: Exception)
}

/**
 * Command for print all tasks from [dataSource]
 *
 * @param dataSource
 * @property command params of command
 *
 * @see Command
 * @see PrintTaskByIdCommand
 */
class PrintAllTasksCommand(dataSource: TasksDataSource, private val command: List<String>) : Command(dataSource) {

    private var tasks: List<Task>? = null

    /**
     * Get all tasks from [dataSource] and call [onSuccess] if result [Result.Success] or [onFail] if result is [Result.Error]
     */
    override fun run() {
        val res = dataSource.getTasks()
        if (res is Result.Success) {
            tasks = when {
                command.size == 2 && command[1] == "done" -> res.data.filter { it.done }
                command.size == 2 && command[1] == "active" -> res.data.filter { !it.done }
                else -> res.data
            }
            onSuccess()
        }

        if (res is Result.Error) onFail(res.exception)
    }

    /**
     * Print [tasks] when [run] execution is success
     */
    override fun onSuccess() {
        println("Tasks:")
        tasks?.forEach {
            println("id: ${it.id}")
            println("name: ${it.name}")
            println("Done: ${it.done}")
            println("=".repeat(10))
        }
    }

    /**
     * Print error message when [run] execution is fail
     */
    override fun onFail(e: Exception) {
        println(e.message)
    }

    companion object {

        /**
         * Check params [list] for create [PrintAllTasksCommand]
         *
         * @return 'true' when first element of [list] is 'tasks' and second element of [list] not exists or equals 'done' or 'active'
         * @throws CommandFormatException when second element of [list] exists and not equals 'done' or 'active'
         */
        fun canCreate(list: List<String>): Boolean {
            val result = list.isNotEmpty() && list[0] == "tasks"

            if (result && list.size == 2 && list[1] != "done" && list[1] != "active") {
                throw CommandFormatException("Unknown param ${list[1]} for tasks command")
            }

            return result
        }
    }
}

/**
 * Command for print task by [id] from [dataSource]
 *
 * @param dataSource
 * @property id the id of [Task]
 *
 * @see Command
 * @see PrintAllTasksCommand
 */
class PrintTaskByIdCommand(dataSource: TasksDataSource, private val id: Long) : Command(dataSource) {

    private var task: Task? = null

    /**
     * Get task by [id] from [dataSource] and call [onSuccess] if result [Result.Success] or [onFail] if result is [Result.Error]
     */
    override fun run() {
        val res = dataSource.getTask(id)
        if (res is Result.Success) {
            task = res.data
            onSuccess()
        }

        if (res is Result.Error) onFail(res.exception)
    }

    /**
     * Print [task] when [run] execution is success
     */
    override fun onSuccess() {
        task?.let {
            println("id: ${it.id}")
            println("name: ${it.name}")
            println("Done: ${it.done}")
            println("")
        }
    }

    /**
     * Print error message when [run] execution is fail
     */
    override fun onFail(e: Exception) {
        println(e.message)
    }

    companion object {

        /**
         * Check params [list] for create [PrintTaskByIdCommand]
         *
         * @return 'true' when first element of [list] is 'task' and second element can convert to [Long]
         * @throws CommandFormatException when second element of [list] not exist or cant't convert to [Long]
         */
        fun canCreate(list: List<String>): Boolean {
            val result = list.isNotEmpty() && list[0] == "task"

            if(result && list.size == 1) throw CommandFormatException("you should add id of task")
            if(result && list.size == 2 && !list[1].canConvertToLong()) throw CommandFormatException("${list[1]} is not digit")

            return result
        }
    }
}

/**
 * Command for delete task by [id] from [dataSource]
 *
 * @param dataSource
 * @property id the id of [Task]
 *
 * @see Command
 * @see DeleteAllTasksCommand
 */
class DeleteTaskByIdCommand(dataSource: TasksDataSource, private val id: Long) : Command(dataSource) {

    /**
     * Delete task by [id] from [dataSource] and call [onSuccess] if result success or [onFail] if result is fail
     */
    override fun run() {
        try {
            dataSource.deleteTask(id)
            onSuccess()
        } catch (e: Exception) {
            onFail(e)
        }
    }

    /**
     * Print that task with [id] deleted success, when [run] execution is success
     */
    override fun onSuccess() {
        println("Task with id $id deleted")
    }

    /**
     * Print error message when [run] execution is fail
     */
    override fun onFail(e: Exception) {
        println(e.message)
    }

    companion object {

        /**
         * Check params [list] for create [DeleteTaskByIdCommand]
         *
         * @return 'true' when first element of [list] is 'delete' and second element can convert to [Long]
         * @throws CommandFormatException when second element of [list] not exist or cant't convert to [Long]
         */
        fun canCreate(list: List<String>): Boolean {

            val result = list.isNotEmpty() && list[0] == "delete"

            if(result && list.size == 1) throw CommandFormatException("you should add id of task")
            if(result && !(list.size == 2 && list[1].canConvertToLong()))
                throw CommandFormatException("${list[1]} is not digit")

            return result
        }
    }
}

/**
 * Command for delete all tasks from [dataSource]
 *
 * @param dataSource
 *
 * @see Command
 * @see DeleteTaskByIdCommand
 */
class DeleteAllTasksCommand(dataSource: TasksDataSource) : Command(dataSource) {

    /**
     * Delete all tasks by from [dataSource] and call [onSuccess] if result success or [onFail] if result is fail
     */
    override fun run() {
        try {
            dataSource.deleteAllTasks()
            onSuccess()
        } catch (e: Exception) {
            onFail(e)
        }
    }

    /**
     * Print that all tasks deleted success, when [run] execution is success
     */
    override fun onSuccess() {
        println("All tasks are deleted")
    }

    /**
     * Print error message when [run] execution is fail
     */
    override fun onFail(e: Exception) {
        println(e.message)
    }

    companion object {
        /**
         * Check params [list] for create [DeleteAllTasksCommand]
         *
         * @return 'true' when first element of [list] is 'delete-all'
         */
        fun canCreate(list: List<String>): Boolean {
            return list.isNotEmpty() && list[0] == "delete-all"
        }
    }
}

/**
 * Command for complete task by [taskId] from [dataSource]
 *
 * @param dataSource
 * @property taskId the id of [Task]
 *
 * @see Command
 * @see ActivateTaskByIdCommand
 */
class CompleteTaskByIdCommand(dataSource: TasksDataSource, private val taskId: Long) : Command(dataSource) {

    /**
     * Complete task with id [taskId] in [dataSource] and call [onSuccess] if result success or [onFail] if result is fail
     */
    override fun run() {
        try {
            dataSource.completeTask(taskId, true)
            onSuccess()
        } catch (e: Exception) {
            onFail(e)
        }
    }

    /**
     * Print that task with [taskId] completed success, when [run] execution is success
     */
    override fun onSuccess() {
        println("Task with id $taskId completed")
    }

    /**
     * Print error message when [run] execution is fail
     */
    override fun onFail(e: Exception) {
        println(e.message)
    }

    companion object {

        /**
         * Check params [list] for create [CompleteTaskByIdCommand]
         *
         * @return 'true' when first element of [list] is 'complete' and second element can convert to [Long]
         * @throws CommandFormatException when second element of [list] not exist or cant't convert to [Long]
         */
        fun canCreate(list: List<String>): Boolean {
            val result = list.isNotEmpty() && list[0] == "complete"

            if(result && list.size == 1) throw CommandFormatException("you should add id of task")
            if(result && list.size == 2 && !list[1].canConvertToLong()) throw CommandFormatException("${list[1]} is not digit")
            return result
        }
    }
}

/**
 * Command for activate task by [taskId] from [dataSource]
 *
 * @param dataSource
 * @property taskId the id of [Task]
 *
 * @see Command
 * @see CompleteTaskByIdCommand
 */
class ActivateTaskByIdCommand(dataSource: TasksDataSource, private val taskId: Long) : Command(dataSource) {

    /**
     * Activate task with id [taskId] in [dataSource] and call [onSuccess] if result success or [onFail] if result is fail
     */
    override fun run() {
        try {
            dataSource.completeTask(taskId, false)
            onSuccess()
        } catch (e: Exception) {
            onFail(e)
        }
    }

    /**
     * Print that task with [taskId] activated success, when [run] execution is success
     */
    override fun onSuccess() {
        println("Task with id: $taskId activated")
    }

    /**
     * Print error message when [run] execution is fail
     */
    override fun onFail(e: Exception) {
        println(e.message)
    }

    companion object {

        /**
         * Check params [list] for create [ActivateTaskByIdCommand]
         *
         * @return 'true' when first element of [list] is 'activate' and second element can convert to [Long]
         * @throws CommandFormatException when second element of [list] not exist or cant't convert to [Long]
         */
        fun canCreate(list: List<String>): Boolean {
            val result = list.isNotEmpty() && list[0] == "activate"

            if(result && list.size == 1) throw CommandFormatException("you should add id of task")
            if(result && list.size == 2 && !list[1].canConvertToLong()) throw CommandFormatException("${list[1]} is not digit")
            return result
        }
    }
}

/**
 * Command for create task with [taskName] from [dataSource]
 *
 * @param dataSource
 * @property taskName the name of [Task]
 *
 * @see Command
 */
class CreateTaskCommand(dataSource: TasksDataSource, private val taskName: String) : Command(dataSource) {

    /**
     * Create task with name [taskName] in [dataSource] and call [onSuccess] if result success or [onFail] if result is fail
     */
    override fun run() {
        try {
            dataSource.saveTask(Task(taskName))
            onSuccess()
        } catch (e: Exception) {
            onFail(e)
        }
    }

    /**
     * Print that task with name [taskName] created success, when [run] execution is success
     */
    override fun onSuccess() {
        println("Task with name $taskName created")
    }

    /**
     * Print error message when [run] execution is fail
     */
    override fun onFail(e: Exception) {
        println(e.message)
    }

    companion object {

        /**
         * Check params [list] for create [CreateTaskCommand]
         *
         * @return 'true' when first element of [list] is 'create' and elements more than 1
         * @throws CommandFormatException when [list] size equals '1'
         */
        fun canCreate(list: List<String>): Boolean {
            val result = list.isNotEmpty() && list[0] == "create"

            if(result && list.size == 1) throw CommandFormatException("task name not found")

            return result
        }
    }
}