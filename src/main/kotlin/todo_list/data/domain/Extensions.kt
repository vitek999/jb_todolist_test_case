package todo_list.data.domain

/**
 * Return map from [List] of tasks, where [Map.Entry.key] is [Task.id] and [Map.Entry.value] is object of [Task]
 *
 * @return map where [Map.Entry.key] is [Task.id] and [Map.Entry.value] is object of [Task]
 */
fun List<Task>.toMap() : Map<Long, Task> = map {it.id to it}.toMap()