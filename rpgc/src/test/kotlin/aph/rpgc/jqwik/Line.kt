package aph.rpgc.jqwik

import aph.util.containsAny
import aph.util.filterNot
import net.jqwik.api.Arbitrary
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.arbitraries.StringArbitrary
import net.jqwik.api.configurators.ArbitraryConfiguratorBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Marks an arbitrary [String] representing a single line of source text.
 *
 * Generated strings satisfy these requirements:
 * - Single-line (do not contain line breaks, carriage returns, or similar characters).
 * - Trimmed (begins and ends in non-whitespace).
 * - Not blank.
 * - Contain only characters belonging to the Unicode range 0000 - 036F.
 *
 * Applies to parameters which are also annotated with [net.jqwik.api.ForAll].
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Line

class LineConfigurator : ArbitraryConfiguratorBase() {

    @Suppress("UNUSED_PARAMETER", "unused")
    fun configure(arbitrary: StringArbitrary, annotation: Line): Arbitrary<String> = configure(arbitrary)

    fun configure(arbitrary: StringArbitrary): Arbitrary<String> = arbitrary
        .withCharRange(CHAR_RANGE)
        .map { it.filterNot(LINE_BREAK_CHARS) }
        .map { it.trim() }.filter { it.isNotBlank() }

    companion object {
        internal val CHAR_RANGE = '\u0000'..'\u036f'
        internal val LINE_BREAK_CHARS = setOf('\n', '\r', '\u0085')
    }
}

fun StringArbitrary.lines(): Arbitrary<String> = LineConfigurator().configure(this)

internal class LineConfiguratorTests {

    @Property fun `Single-line`(@ForAll @Line s: String) {
        assertFalse(s.containsAny(LineConfigurator.LINE_BREAK_CHARS))
    }

    @Property fun `Trimmed`(@ForAll @Line s: String) {
        assertEquals(s, s.trim())
    }

    @Property fun `Non-blank`(@ForAll @Line s: String) {
        assertTrue(s.isNotBlank())
    }

    @Property fun `Standard character range`(@ForAll @Line s: String) {
        assertTrue(s.all { it in LineConfigurator.CHAR_RANGE })
    }
}
