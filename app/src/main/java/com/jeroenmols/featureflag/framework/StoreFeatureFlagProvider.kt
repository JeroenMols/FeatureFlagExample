package com.jeroenmols.featureflag.framework

import com.jeroenmols.featureflag.framework.FeatureFlag.DARK_MODE

class StoreFeatureFlagProvider : FeatureFlagProvider {

    override val priority = MIN_PRIORITY

    @Suppress("ComplexMethod")
    override fun isFeatureEnabled(feature: Feature): Boolean {
        if (feature is FeatureFlag) {
            // No "else" branch here -> choosing the default option for release must be an explicit choice
            return when (feature) {
                DARK_MODE -> false
            }
        } else {
            // TestSettings should never be shipped to users
            return when (feature as TestSetting) {
                else -> false
            }
        }
    }

    override fun hasFeature(feature: Feature): Boolean = true
}
