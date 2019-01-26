package aph.rpgc.parse

import aph.rpgc.core.GenericElement
import aph.rpgc.jqwik.Line
import aph.rpgc.jqwik.OfParagraphs
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import org.junit.jupiter.api.Assertions.assertEquals

class GenericElementGrammarTests {

    @Property fun `Can parse empty element`(@ForAll @Line elementName: String) {
        assertEquals(
            GenericElement(name = elementName),
            GenericElementGrammar.parseToEnd("# $elementName")
        )
    }

    @Property(tries = 500) fun `Can parse element with just name and description`(
        @ForAll @Line elementName: String,
        @ForAll @OfParagraphs description: List<String>
    ) {
        assertEquals(
            GenericElement(name = elementName, description = description.map { it.replace('\n', ' ') }),
            GenericElementGrammar.parseToEnd("""
                |# $elementName
                ${description.joinToString(separator = "\n\n").prependIndent("|")}
            """.trimMargin())
        )
    }
}
