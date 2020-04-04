package todo_list.utils

/**
 * Return result of check that [this] [String] can parse to [Long]
 *
 * @return 'true' when [this] [String] can parse to [Long]
 */
fun String.canConvertToLong() : Boolean {
    var res = true

    try {
        toLong()
    } catch (e : NumberFormatException) {
        res = false
    }

    return res
}