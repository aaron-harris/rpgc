package aph.rpgc.parse

import aph.rpgc.core.GenericElement
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import net.jqwik.api.Assume
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.constraints.CharRange
import org.junit.jupiter.api.Assertions.assertEquals

class GenericElementGrammarTests {

    @CharRange(from = '\u0000', to = '\u036f')
    annotation class OrdinaryText

    @Property fun `Can parse empty element`(@ForAll @OrdinaryText s: String) {
        val elementName = s.trim()
        Assume.that(elementName.isNotBlank())
        Assume.that(!elementName.contains('\n'))
        Assume.that(!elementName.contains('\r'))
        Assume.that(!elementName.contains('\u0085')) // Next line control character
        assertEquals(
            GenericElement(name = elementName),
            GenericElementGrammar.parseToEnd("# $elementName")
        )
    }
}
