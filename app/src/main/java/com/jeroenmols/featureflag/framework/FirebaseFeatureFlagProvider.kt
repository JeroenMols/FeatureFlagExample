package com.jeroenmols.featureflag.framework

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class FirebaseFeatureFlagProvider(private val isDevModeEnabled: Boolean) : FeatureFlagProvider, RemoteFeatureFlagProvider {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(isDevModeEnabled).build()
        remoteConfig.setConfigSettings(configSettings)
    }

    override val priority: Int = MAX_PRIORITY

    override fun isFeatureEnabled(feature: Feature): Boolean =
        remoteConfig.getBoolean(feature.key)

    override fun hasFeature(feature: Feature): Boolean {
        return when (feature) {
            FeatureFlag.DARK_MODE -> true
            else -> false
        }
    }

    override fun refreshFeatureFlags() {
        remoteConfig.fetch(getCacheExpirationSeconds(isDevModeEnabled)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // After config data is successfully fetched, it must be activated before newly fetched values are returned.
                remoteConfig.activateFetched()
            }
        }
    }

    private fun getCacheExpirationSeconds(isDevModeEnabled: Boolean): Long = if (isDevModeEnabled) {
        CACHE_EXPIRATION_SECS_DEV
    } else {
        CACHE_EXPIRATION_SECS
    }

    companion object {
        const val CACHE_EXPIRATION_SECS = 1 * 60 * 60L
        const val CACHE_EXPIRATION_SECS_DEV = 1L
    }
}