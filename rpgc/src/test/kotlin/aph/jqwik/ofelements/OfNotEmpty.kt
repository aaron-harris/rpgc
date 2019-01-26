package aph.jqwik.ofelements

import net.jqwik.api.Arbitrary
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.arbitraries.SizableArbitrary
import net.jqwik.api.arbitraries.StringArbitrary
import net.jqwik.api.configurators.ArbitraryConfiguratorBase
import net.jqwik.api.constraints.NotEmpty
import org.junit.jupiter.api.Assertions.assertTrue

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class OfNotEmpty

@Suppress("UNUSED_PARAMETER", "unused")
class OfNotEmptyConfigurator : ArbitraryConfiguratorBase(), OfElementsConfigurator {
    fun configure(elementArbitrary: StringArbitrary, annotation: OfNotEmpty): Arbitrary<String> =
        elementArbitrary.ofMinLength(1)

    fun configure(elementArbitrary: SizableArbitrary<*>, annotation: OfNotEmpty): SizableArbitrary<*> =
        elementArbitrary.ofMinSize(1)
}

internal class OfNotEmptyConfiguratorTests {

    @Property fun `Configuration is applied`(@ForAll @NotEmpty @OfNotEmpty xs: List<String>) {
        assertTrue(xs.all { it.isNotEmpty() })
    }
}
