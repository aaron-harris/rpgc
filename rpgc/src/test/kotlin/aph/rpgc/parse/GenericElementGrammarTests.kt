package aph.rpgc.parse

import aph.jqwik.Index
import aph.rpgc.core.GenericElement
import aph.rpgc.jqwik.Line
import aph.rpgc.jqwik.OfLines
import aph.rpgc.jqwik.OfParagraphs
import aph.rpgc.parse.GenericElementGrammar.NAME_MARKER
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.parser.ParseException
import net.jqwik.api.Assume
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class GenericElementGrammarTests {

    @Property fun `Can parse empty element`(@ForAll @Line elementName: String) {
        assertEquals(
            GenericElement(name = elementName),
            GenericElementGrammar.parseToEnd(NAME_MARKER + elementName)
        )
    }

    @Property(tries = 500) fun `Can parse element with just name and description`(
        @ForAll @Line elementName: String,
        @ForAll @OfParagraphs description: List<String>
    ) {
        Assume.that(description.none { it.startsWith(NAME_MARKER) })
        assertEquals(
            GenericElement(name = elementName, description = description.map { it.replace('\n', ' ') }),
            GenericElementGrammar.parseToEnd("""
                |${NAME_MARKER + elementName}
                ${description.joinToString(separator = "\n\n").prependIndent("|")}
            """.trimMargin())
        )
    }

    @Property fun `No line except for the first may start with the name marker`(
        @ForAll @OfLines lines: List<String>,
        @ForAll @Index index: Int
    ) {
        Assume.that(index in lines.indices)
        val poisonedText = lines.mapIndexed { lineNumber, line ->
            (if (lineNumber == index) NAME_MARKER else "") + line
        }.joinToString(separator = "\n")

        assertThrows<ParseException> {
            GenericElementGrammar.parseToEnd(NAME_MARKER + poisonedText)
        }
    }
}
