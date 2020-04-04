package todo_list

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import todo_list.data.TEST_FILES_DIR
import java.io.File

class FileCheckTest {

    @Test
    fun `exception when file not exists`() {
        val file = File("$TEST_FILES_DIR/not-exists-file.json")

        assertThrows<JsonFileNotExistsException> { MainApplication.fileCheck(file) }
    }

    @Test
    fun `exception when file is blank`() {
        val file = File("$TEST_FILES_DIR/blank.json")

        assertThrows<JsonFileIsBlankException> { MainApplication.fileCheck(file) }
    }

    @Test
    fun `exception when file has wrong format`() {
        val file = File("$TEST_FILES_DIR/wrong-format.json")

        assertThrows<JsonFileHasWrongFormatException> { MainApplication.fileCheck(file) }
    }

    @Test
    fun `ok check file with correct content`() {
        val file = File("$TEST_FILES_DIR/test-json.json")

        assertTrue(MainApplication.fileCheck(file))
    }
}