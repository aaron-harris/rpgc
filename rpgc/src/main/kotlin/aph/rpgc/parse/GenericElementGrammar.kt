package aph.rpgc.parse

import aph.rpgc.core.GenericElement
import aph.rpgc.core.Label
import aph.rpgc.core.Paragraph
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.oneOrMore
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

    private val terminatedLine: Parser<String> by line and skip(optional(newline)) use { text }
    private val paragraph: Parser<Paragraph> by oneOrMore(terminatedLine) use { joinToString(separator = " ") }

    private val name: Parser<Label> by skip(nameStartMarker) and terminatedLine
    private val description: Parser<List<Paragraph>> by separatedTerms(paragraph, newline, acceptZero = true)

    override val rootParser: Parser<GenericElement> by name and description use {
        GenericElement(name = t1, description = t2)
    }
}
