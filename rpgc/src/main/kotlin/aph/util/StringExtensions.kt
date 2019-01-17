package aph.util

/** Returns `true` if this char sequence contains at least one element of [chars].*/
fun CharSequence.containsAny(chars: Collection<Char>): Boolean = any { it in chars }

/** Returns a copy of this string with all characters in [chars] removed.*/
fun String.filterNot(chars: Collection<Char>): String = filterNot { it in chars }
