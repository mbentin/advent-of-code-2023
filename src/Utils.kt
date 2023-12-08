import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

inline fun <T> Iterable<T>.multiplicationOf(selector: (T) -> Int): Int {
    var multiplication = 1
    for (element in this) {
        multiplication *= selector(element)
    }
    return multiplication
}

inline fun <T> Sequence<T>.multiplicationOf(selector: (T) -> Int): Int {
    var multiplication = 1
    for (element in this) {
        multiplication *= selector(element)
    }
    return multiplication
}

fun gcd(a: Long, b: Long): Long {
    var a = a
    var b = b
    while (b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
