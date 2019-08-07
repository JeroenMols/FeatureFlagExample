package com.jeroenmols.featureflag.framework

import android.content.Context
import android.content.SharedPreferences
import com.jeroenmols.featureflag.framework.TestSetting.IDLING_RESOURCES
import com.jeroenmols.featureflag.framework.TestSetting.LEAK_CANARY
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock

class RuntimeBehaviorTest {

    private val feature_defaultoff = IDLING_RESOURCES
    private val feature_defaulton = LEAK_CANARY

    @Before
    fun setUp() {
        RuntimeBehavior.clearFeatureFlagProviders()
    }

    @Test
    fun `should return default value when no provider`() {
        assertThat(RuntimeBehavior.isFeatureEnabled(feature_defaultoff)).isEqualTo(
            feature_defaultoff.defaultValue
        )
    }

    @Test
    fun `should get value from provider when added`() {
        RuntimeBehavior.addProvider(TestProvider())

        assertThat(RuntimeBehavior.isFeatureEnabled(feature_defaultoff)).isTrue()
    }

    @Test
    fun `should get default value when provider doesn't have value`() {
        RuntimeBehavior.addProvider(TestProvider())

        assertThat(RuntimeBehavior.isFeatureEnabled(feature_defaulton)).isEqualTo(feature_defaulton.defaultValue)
    }

    @Test
    fun `should get default value from highest priority provider`() {
        RuntimeBehavior.addProvider(TestProvider())
        RuntimeBehavior.addProvider(MaxPriorityTestProvider())

        assertThat(RuntimeBehavior.isFeatureEnabled(feature_defaultoff)).isEqualTo(false)
    }

    @Test
    fun `should call refresh feature flag when provider has a RemoteFeatureFlagProvider`() {
        val provider = TestProvider()

        RuntimeBehavior.addProvider(provider)
        RuntimeBehavior.refreshFeatureFlags()

        assertThat(provider.featureFlagsRefreshed).isTrue()
    }

    @Test
    fun `should have runtimeProvider in debug build`() {
        val context = mock(Context::class.java)
        whenever(
            context.getSharedPreferences(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(
            mock(
                SharedPreferences::class.java
            )
        )
        RuntimeBehavior.initialize(context, true)

        assertThat(RuntimeBehavior.providers[0]).isInstanceOf(RuntimeFeatureFlagProvider::class.java)
    }

    @Test
    fun `should only have two providers in debug build`() {
        val context = mock(Context::class.java)
        whenever(context.getSharedPreferences(anyString(), anyInt())).thenReturn(mock())
        RuntimeBehavior.initialize(context, true)

        assertThat(RuntimeBehavior.providers).hasSize(2)
    }

    inner class TestProvider : FeatureFlagProvider, RemoteFeatureFlagProvider {

        var featureFlagsRefreshed: Boolean = false

        override fun refreshFeatureFlags() {
            featureFlagsRefreshed = true
        }

        override val priority = MIN_PRIORITY

        override fun isFeatureEnabled(feature: Feature): Boolean = true

        override fun hasFeature(feature: Feature): Boolean = feature == feature_defaultoff
    }

    inner class MaxPriorityTestProvider : FeatureFlagProvider {

        override val priority = MAX_PRIORITY

        override fun isFeatureEnabled(feature: Feature): Boolean = false

        override fun hasFeature(feature: Feature): Boolean = true
    }
}
