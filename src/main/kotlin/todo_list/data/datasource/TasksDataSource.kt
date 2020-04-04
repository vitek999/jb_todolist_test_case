package todo_list.data.datasource

import todo_list.data.Result
import todo_list.data.domain.Task

/**
 * Interface for working with different tasks datasource
 */
interface TasksDataSource {

    /**
     * Get all tasks from datasource
     *
     * @return list of tasks from datasource
     */
    fun getTasks(): Result<List<Task>>

    /**
     * Get task by id from datasource
     *
     * @param taskId id of task
     *
     * @return [Result.Success] if datasource contains id with [taskId] or [Result.Error] with [TaskNotFoundException] if task not found
     */
    fun getTask(taskId: Long): Result<Task>

    /**
     * Save task into datasource
     *
     * When task saving id of task set on one more than max id from datasource
     *
     * @param task task for save in datasource
     */
    fun saveTask(task: Task)

    /**
     * Set complete status of task with [taskId] in datasource
     *
     * @param taskId id of task
     * @param value new complete status of task with [taskId]
     * @throws TaskNotFoundException when datasource not contains task with [taskId]
     */
    fun completeTask(taskId: Long, value: Boolean)

    /**
     * Delete task from datasource by [taskId]
     *
     * @param taskId id of task
     * @throws TaskNotFoundException when datasource not contains task with [taskId]
     */
    fun deleteTask(taskId: Long)

    /**
     * Delete all tasks from datasource
     */
    fun deleteAllTasks()
}