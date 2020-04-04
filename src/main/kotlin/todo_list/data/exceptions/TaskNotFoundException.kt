package todo_list.data.exceptions

import java.lang.RuntimeException

/**
 * Throw this exception where [Task] with id not found
 */
class TaskNotFoundException(id: Long) : RuntimeException("Task with id: $id not found")