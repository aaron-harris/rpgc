package aph.rpgc.parse

import aph.rpgc.core.GenericElement
import aph.rpgc.core.Label
import aph.rpgc.core.Paragraph
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.parser.Parser

object GenericElementGrammar : Grammar<GenericElement>() {

    private val nameStartMarker by token("# ")

    private val line by token(".+")
    private val newline by token("\n")

    private val paragraphParser: Parser<Paragraph> by separatedTerms(line, newline) use {
        joinToString(separator = " ") { it.text }
    }

    private val nameParser: Parser<Label> by skip(nameStartMarker) and line and skip(optional(newline)) use { text }
    private val descriptionParser: Parser<List<Paragraph>> by optional(paragraphParser) map { listOfNotNull(it) }

    override val rootParser: Parser<GenericElement> by nameParser and descriptionParser use {
        GenericElement(name = t1, description = t2)
    }
}
