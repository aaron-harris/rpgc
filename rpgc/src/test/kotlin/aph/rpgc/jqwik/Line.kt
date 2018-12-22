package aph.rpgc.jqwik

import net.jqwik.api.Arbitrary
import net.jqwik.api.arbitraries.StringArbitrary
import net.jqwik.api.configurators.ArbitraryConfiguratorBase

/**
 * An arbitrary [String] representing a single line of source text.
 *
 * Generated strings satisfy these requirements:
 * - Single-line (do not contain line breaks, carriage returns, or similar characters).
 * - Trimmed (ends in non-whitespace).
 * - Contain only characters belonging to the Unicode range 0000 - 036F.
 *
 * Applies to parameters which are also annotated with [net.jqwik.api.ForAll].
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Line

@Suppress("UNUSED_PARAMETER", "unused")
class LineConfigurator : ArbitraryConfiguratorBase() {

    fun configure(
        arbitrary: StringArbitrary,
        annotation: Line
    ): Arbitrary<String> = arbitrary
        .withCharRange(CHAR_RANGE)
        .filter { !it.contains(LINE_BREAK_REGEX) }
        .map { it.trim() }.filter { it.isNotBlank() }

    companion object {
        private val CHAR_RANGE = '\u0000'..'\u036f'
        private val LINE_BREAK_REGEX = "[\n\r\u0085]".toRegex()
    }
}
