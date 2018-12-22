package aph.rpgc.jqwik

import net.jqwik.api.arbitraries.StringArbitrary

fun StringArbitrary.withCharRange(range: CharRange): StringArbitrary = withCharRange(range.first, range.last)
