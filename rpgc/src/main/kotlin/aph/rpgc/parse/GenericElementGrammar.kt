package aph.rpgc.parse

import aph.rpgc.core.GenericElement
import aph.rpgc.core.Label
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.parser.Parser

object GenericElementGrammar : Grammar<GenericElement>() {

    private val nameStartMarker by token("# ")
    private val name by token(".+")

    private val nameParser: Parser<Label> by skip(nameStartMarker) and name use { text }

    override val rootParser: Parser<GenericElement> by nameParser map {
        GenericElement(name = it)
    }
}
