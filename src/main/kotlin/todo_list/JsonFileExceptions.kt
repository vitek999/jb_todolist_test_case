package todo_list

import java.lang.RuntimeException

/**
 * Throw this exception when json file with name [fileName] is incorrect
 *
 * @param fileName name of json file
 */
open class JsonFileException(fileName: String) : RuntimeException(fileName)

/**
 * Throw this exception when json file with name [fileName] not exists
 *
 * @param fileName name of json file
 */
class JsonFileNotExistsException(fileName: String) : JsonFileException("File '$fileName' not exists")

/**
 * Throw this exception when json file with name [fileName] is blank
 *
 * @param fileName name of json file
 */
class JsonFileIsBlankException(fileName: String) : JsonFileException("File '$fileName' is blank")

/**
 * Throw this exception when json file with name [fileName] has wrong json format
 *
 * @param fileName name of json file
 */
class JsonFileHasWrongFormatException(fileName: String) : JsonFileException("File '$fileName' has wrong json format")

/**
 * Throw this exception when json file with name [fileName] contains a lot of records
 *
 * @param fileName name of json file
 */
class JsonFileContainsALotOfRecordsExceptions(fileName: String) : JsonFileException("File '$fileName' contains a lot of records")