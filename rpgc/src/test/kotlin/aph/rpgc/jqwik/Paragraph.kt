package aph.rpgc.jqwik

import aph.jqwik.ofelements.OfElementsConfigurator
import net.jqwik.api.Arbitrary
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.arbitraries.StringArbitrary
import net.jqwik.api.configurators.ArbitraryConfiguratorBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Marks an arbitrary [String] that represents a paragraph of source text.
 *
 * Generated strings satisfy these requirements:
 * - Use Unix line separators.
 * - Contain no blank lines.
 * - Trimmed (ends in non-whitespace).
 * - Contain only characters belonging to the Unicode range 0001 - 036F.
 *
 * Applies to parameters which are also annotated with [net.jqwik.api.ForAll].
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Paragraph

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class OfParagraphs

@Suppress("UNUSED_PARAMETER", "unused")
class ParagraphConfigurator : ArbitraryConfiguratorBase(), OfElementsConfigurator {

    fun configure(arbitrary: StringArbitrary, annotation: Paragraph): Arbitrary<String> = configure(arbitrary)
    fun configure(arbitrary: StringArbitrary, annotation: OfParagraphs): Arbitrary<String> = configure(arbitrary)

    private fun configure(arbitrary: StringArbitrary): Arbitrary<String> = arbitrary
        .lines()
        .list().filter { it.isNotEmpty() }
        .map { it.joinToString(separator = "\n") }

    companion object {
        internal val CHAR_RANGE = LineConfigurator.CHAR_RANGE
    }
}

internal class ParagraphConfiguratorTests {

    @Property fun `Unix line separators`(@ForAll @Paragraph p: String) {
        assertFalse(p.contains("\r"))
    }

    @Property fun `Contains no blank lines`(@ForAll @Paragraph p: String) {
        assertFalse(p.lines().any { it.isBlank() })
    }

    @Property fun `Trimmed`(@ForAll @Paragraph p: String) {
        assertEquals(p, p.trim())
    }

    @Property fun `Standard character range`(@ForAll @Paragraph p: String) {
        assertTrue(p.all { it in ParagraphConfigurator.CHAR_RANGE })
    }

    @Property(tries = 100) fun `Configuration for @OfParagraphs is applied correctly`(
        @ForAll @OfParagraphs xs: List<String>
    ) {
        xs.forEach {
            `Unix line separators`(it)
            `Contains no blank lines`(it)
            `Trimmed`(it)
            `Standard character range`(it)
        }
    }
}
