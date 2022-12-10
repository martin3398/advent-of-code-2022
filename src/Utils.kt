import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: Int, test: Boolean = false): List<String> {
    val dayStr = day.toString().padStart(2, '0')
    val testSuffix = if (test) "_test" else ""
    return File("src/day$dayStr", "Day$dayStr${testSuffix}.txt").readLines()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
