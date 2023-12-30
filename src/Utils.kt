import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
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

private class MemoizedHandler<F, in K : MemoizedCall<F, R>, out R>(val f: F) {
    private val m = ConcurrentHashMap<K, R>()
    operator fun invoke(k: K): R {
        return m[k] ?: run {
            val r = k(f)
            m.putIfAbsent(k, r)
            r
        }
    }
}

fun <P1, P2, R> ((P1, P2) -> R).memoize(): (P1, P2) -> R {
    return object : (P1, P2) -> R {
        private val m = MemoizedHandler<((P1, P2) -> R), MemoizeKey2<P1, P2, R>, R>(this@memoize)
        override fun invoke(p1: P1, p2: P2) = m(MemoizeKey2(p1, p2))
    }
}

private interface MemoizedCall<in F, out R> {
    operator fun invoke(f: F): R
}
private data class MemoizeKey2<out P1, out P2, R>(val p1: P1, val p2: P2) : MemoizedCall<(P1, P2) -> R, R> {
    override fun invoke(f: (P1, P2) -> R) = f(p1, p2)
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
