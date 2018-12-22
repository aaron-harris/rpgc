package aph.rpgc.parse

import aph.rpgc.core.GenericElement
import aph.rpgc.jqwik.Line
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
}
