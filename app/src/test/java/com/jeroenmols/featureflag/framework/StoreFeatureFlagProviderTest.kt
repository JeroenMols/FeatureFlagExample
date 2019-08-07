package com.jeroenmols.featureflag.framework

import com.jeroenmols.featureflag.framework.TestSetting.IDLING_RESOURCES
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StoreFeatureFlagProviderTest {
    @Test
    fun `has lowest priority`() {
        assertThat(StoreFeatureFlagProvider().priority).isEqualTo(MIN_PRIORITY)
    }

    @Test
    fun `has value for every toggle`() {
        assertThat(StoreFeatureFlagProvider().hasFeature(IDLING_RESOURCES)).isTrue()
    }

    @Test
    fun `idling resources off by default`() {
        assertThat(StoreFeatureFlagProvider().isFeatureEnabled(IDLING_RESOURCES)).isFalse()
    }

    // more tests here
}
