package aph.jqwik.ofelements

import net.jqwik.api.Arbitrary
import net.jqwik.api.Assume
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.configurators.ArbitraryConfigurator
import net.jqwik.api.configurators.SelfConfiguringArbitrary
import net.jqwik.api.constraints.NotEmpty
import net.jqwik.api.constraints.Size
import net.jqwik.api.providers.TypeUsage
import net.jqwik.properties.arbitraries.ListArbitrary
import net.jqwik.providers.ListArbitraryProvider
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * A list arbitrary that handles configuration on behalf of its elements.
 *
 * This is a workaround for use until Kotlin supports placing annotations on generic type parameters.  It replaces the
 * default list arbitrary and supports specialized `@Of*` annotations that can be placed on the list type rather than
 * the element type.  For example, a parameter of the form
 *
 *     @ForAll xs: List<@NotEmpty String>
 *
 * doesn't work currently (because Kotlin doesn't retain the `@NotEmpty` annotation for jqwik to process).  Instead, use
 * an `@OfNotEmpty` annotation on the list:
 *
 *     @ForAll @OfNotEmpty xs: List<String>
 */
class ElementConfiguringListArbitrary<T>(elementArbitrary: Arbitrary<T>) :
    ListArbitrary<T>(elementArbitrary),
    SelfConfiguringArbitrary<List<T>> {

    private constructor(
        sourceArbitrary: ElementConfiguringListArbitrary<T>,
        elementArbitrary: Arbitrary<T>
    ) : this(elementArbitrary) {
        this.minSize = sourceArbitrary.minSize
        this.maxSize = sourceArbitrary.maxSize
    }

    override fun configure(configurator: ArbitraryConfigurator, targetType: TypeUsage): Arbitrary<List<T>> =
        configurator.configure(this.withConfiguredElements(configurator, targetType), targetType)

    private fun withConfiguredElements(
        configurator: ArbitraryConfigurator,
        targetType: TypeUsage
    ): ElementConfiguringListArbitrary<T> = ElementConfiguringListArbitrary(this, when (configurator) {
        is OfElementsConfigurator -> configurator.configure(elementArbitrary, targetType)
        else -> elementArbitrary
    })
}

/** Marker interface indicating that this configurator performs configuration for the elements of a list arbitrary. */
interface OfElementsConfigurator : ArbitraryConfigurator

class ElementConfiguringListArbitraryProvider : ListArbitraryProvider() {
    override fun create(innerArbitrary: Arbitrary<*>): Arbitrary<*> = ElementConfiguringListArbitrary(innerArbitrary)
    override fun priority(): Int = super.priority() + 1
}

internal class ElementConfiguringListArbitraryTests {

    @Property fun `Annotations on generic parameters vanish (workaround still necessary)`(
        @ForAll xs: List<@NotEmpty String>
    ) {
        Assume.that(xs.any { it.isEmpty() })
    }

    @Property fun `Size constraints on list are retained`(
        @ForAll @Size(min = 1, max = 2) xs: List<*>
    ) {
        assertTrue(xs.size in (1..2))
    }
}
