package todo_list

import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonDecodingException
import todo_list.commands.CommandsFactory
import todo_list.commands.exceptions.CommandFormatException
import todo_list.data.datasource.JsonFileTasksDataSource
import todo_list.data.datasource.TasksDataSource
import todo_list.data.domain.Task
import java.io.File

/**
 * Application class
 * @param file the json file for store tasks
 */
class MainApplication(file: File) {

    private val tasksDataSource: TasksDataSource = JsonFileTasksDataSource(file)
    private val commandsFactory = CommandsFactory(tasksDataSource)

    /**
     * Method for start loop reading command and executing commands from console input
     */
    fun start() {
        while (true) {
            try {
                val command = commandsFactory.create(readLine()!!)
                command.run()
            } catch (e: CommandFormatException) {
                println(e.message)
            }
        }
    }

    companion object {

        private const val MAX_RECORDS_COUNT = 100_000

        @JvmStatic
        private val json: Json = Json(JsonConfiguration.Stable.copy(prettyPrint = true))

        /**
         * Check json file on correct for working with [MainApplication]
         *
         * Check that [file] exists, not blank, has correct json format and not contains a lot of records (more than [MAX_RECORDS_COUNT])
         *
         * @param file the checking file
         * @return [file] check result
         */
        @JvmStatic
        fun fileCheck(file: File): Boolean {
            val exists = file.exists()

            if (!exists) throw JsonFileNotExistsException(file.name)

            val textContent = file.readText()

            if (textContent.isBlank()) throw JsonFileIsBlankException(file.name)

            val records: List<Task>

            try {
                records = json.parse(Task.serializer().list, textContent)
            } catch (e: JsonDecodingException) {
                throw JsonFileHasWrongFormatException(file.name)
            }

            if (records.isNotEmpty() && records.size > MAX_RECORDS_COUNT) {
                throw JsonFileContainsALotOfRecordsExceptions(file.name)
            }
            return true
        }
    }
}

/**
 * Entry point of application
 *
 * @param args[1] - name of you json file. If param empty default file is "todo-list.json"
 */
fun main(args: Array<String>) {
    val file = if (args.size >= 2 && args[1].isNotBlank()) File(args[1]) else File("todo-list.json")

    try {
        if (MainApplication.fileCheck(file)) MainApplication(file).start()
    } catch (e: JsonFileException) {
        println("Error: ${e.message}")
    }
}
