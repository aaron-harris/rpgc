package aph.rpgc.parse

import aph.rpgc.core.GenericElement
import aph.rpgc.jqwik.Line
import aph.rpgc.jqwik.Paragraph
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

    @Property fun `Can parse element with one paragraph description`(
        @ForAll @Line elementName: String,
        @ForAll @Paragraph description: String
    ) {
        assertEquals(
            GenericElement(name = elementName, description = listOf(description.replace('\n', ' '))),
            GenericElementGrammar.parseToEnd("""
                |# $elementName
                ${description.prependIndent("|")}
            """.trimMargin())
        )
    }
}
