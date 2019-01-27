package aph.jqwik

import net.jqwik.api.constraints.IntRange
import net.jqwik.properties.arbitraries.randomized.RandomGenerators

/** Constrains an arbitrary [Int] to the range of valid indices for a default-sized arbitrary collection. */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@IntRange(min = 0, max = RandomGenerators.DEFAULT_COLLECTION_SIZE - 1)
annotation class Index
