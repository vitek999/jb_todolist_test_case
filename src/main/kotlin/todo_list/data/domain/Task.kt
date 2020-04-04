package todo_list.data.domain

import kotlinx.serialization.Serializable

/**
 * Data class for present task
 *
 * @property name the name of task
 * @property done the status of task complete (default = false)
 * @property id the id of task (default = 0L)
 */
@Serializable
data class Task(val name: String, val done: Boolean = false, val id: Long = 0)