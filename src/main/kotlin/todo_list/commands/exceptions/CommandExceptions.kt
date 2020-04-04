package todo_list.commands.exceptions

import java.lang.RuntimeException

/**
 * Throw this when you have wrong params for create [Command]
 */
class CommandFormatException(message: String) : RuntimeException(message)
