package com.jeroenmols.featureflag.framework

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

class RuntimeFeatureFlagProviderTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var preferences: SharedPreferences

    private val MOCK_FEATURE = TestSetting.IDLING_RESOURCES

    @Test
    fun `has medium priority`() {
        assertThat(RuntimeFeatureFlagProvider(preferences).priority).isEqualTo(MEDIUM_PRIORITY)
    }

    @Test
    fun `is feature enabled looks feature up into preferences`() {
        RuntimeFeatureFlagProvider(preferences).isFeatureEnabled(MOCK_FEATURE)

        verify(preferences).getBoolean(MOCK_FEATURE.key, false)
    }

    @Test
    fun `use feature default value when no value in preferences`() {
        RuntimeFeatureFlagProvider(preferences).isFeatureEnabled(MOCK_FEATURE)

        verify(preferences).getBoolean(anyString(), eq(MOCK_FEATURE.defaultValue))
    }

    @Test
    fun `has every feature`() {
        assertThat(RuntimeFeatureFlagProvider(preferences).hasFeature(MOCK_FEATURE)).isTrue()
    }

    @Test
    fun `enable feature should set preference to true`() {
        val mockEditor = mock<SharedPreferences.Editor>()
        whenever(preferences.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(any(), any())).thenReturn(mockEditor)

        RuntimeFeatureFlagProvider(preferences).setFeatureEnabled(MOCK_FEATURE, true)

        verify(mockEditor).putBoolean(MOCK_FEATURE.key, true)
    }

    @Test
    fun `disable feature should set preference to false`() {
        val mockEditor = mock<SharedPreferences.Editor>()
        whenever(preferences.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(any(), any())).thenReturn(mockEditor)

        RuntimeFeatureFlagProvider(preferences).setFeatureEnabled(MOCK_FEATURE, false)

        verify(mockEditor).putBoolean(MOCK_FEATURE.key, false)
    }

    @Test
    fun `enable or disable feature should store preference`() {
        val mockEditor = mock<SharedPreferences.Editor>()
        whenever(preferences.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(any(), any())).thenReturn(mockEditor)

        RuntimeFeatureFlagProvider(preferences).setFeatureEnabled(MOCK_FEATURE, true)

        verify(mockEditor).apply()
    }
}
