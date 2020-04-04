package todo_list.commands

import todo_list.commands.exceptions.CommandFormatException
import todo_list.data.datasource.TasksDataSource

/**
 * Factory for creating command for work with [dataSource] and print results
 *
 * @property dataSource provide [TasksDataSource] for [Command]
 *
 * @see Command
 */
class CommandsFactory(private val dataSource: TasksDataSource) {

    /**
     * Create [Command] from [commandString]
     *
     *
     * @param commandString the command string
     *
     * @return created [Command]
     * @throws [CommandFormatException] when [commandString] is wrong
     */
    fun create(commandString: String): Command {
        val parsedCommand = commandString.split(" ")
        return when {
            PrintAllTasksCommand.canCreate(parsedCommand) -> PrintAllTasksCommand(dataSource, parsedCommand)
            PrintTaskByIdCommand.canCreate(parsedCommand) -> PrintTaskByIdCommand(dataSource, parsedCommand[1].toLong())
            DeleteAllTasksCommand.canCreate(parsedCommand) -> DeleteAllTasksCommand(dataSource)
            DeleteTaskByIdCommand.canCreate(parsedCommand) -> DeleteTaskByIdCommand(dataSource, parsedCommand[1].toLong())
            CompleteTaskByIdCommand.canCreate(parsedCommand) -> CompleteTaskByIdCommand(dataSource, parsedCommand[1].toLong())
            ActivateTaskByIdCommand.canCreate(parsedCommand) -> ActivateTaskByIdCommand(dataSource, parsedCommand[1].toLong())
            CreateTaskCommand.canCreate(parsedCommand) -> CreateTaskCommand(dataSource,
                (1 until parsedCommand.size).joinToString(separator = " ") { parsedCommand[it] })
            else -> throw CommandFormatException("command '${parsedCommand[0]}' not found")
        }
    }

}