package com.jeroenmols.featureflag.framework

/**
 * A Feature uniquely identifies a part of the app code that can either be enabled or disabled.
 * Features only have two states by design to simplify the implementation
 *
 * @param key unique value that identifies a test setting (for "Remote Config tool" flags this is shared across Android/iOS)
 */
interface Feature {
    val key: String
    val title: String
    val explanation: String
    val defaultValue: Boolean
}

/**
 * A feature flag is something that disappears over time (hence it is a tool to simplify development)
 * e.g we develop a feature, test it, release it, then we remove it and the feature remain in the app
 *
 * Note that this has nothing to do with being available as a remote feature flag or not. Some features
 * will be deployed using our feature flag tool, some will not.
 *
 * [key] Shared between Android and iOS featureflag backend
 */
enum class FeatureFlag(
        override val key: String,
        override val title: String,
        override val explanation: String,
        override val defaultValue: Boolean = true
) : Feature {
    DARK_MODE("feature.darkmode", "Dark theme", "Enabled dark mode")
}

/**
 * A test setting is something that stays in our app forever (hence it is a tool to simplify testing)
 * e.g. it is a hook into our app to allow something that a production app shouldn’t allow. (enable logging, bypass software update,…)
 *
 * Test settings must never be exposed via our remote feature flag tool.
 */
enum class TestSetting(
        override val key: String,
        override val title: String,
        override val explanation: String,
        override val defaultValue: Boolean = false
) : Feature {
    USE_DEVELOP_PORTAL("testsetting.usedevelopportal", "Development portal", "Use developer REST endpoint"),
    IDLING_RESOURCES("testsetting.idlingresources", "Idling resources", "Enable idling resources for Espresso"),
    LEAK_CANARY("testsetting.leakcanary", "Leak Canary", "Enable leak canary"),
    DEBUG_LOGGING("testsetting.debuglogging", "Enable logging", "Print all app logging to console", defaultValue = true),
    STRICT_MODE("testsetting.strictmode", "Enable strict mode", "Detect IO operations accidentally performed on the main Thread", defaultValue = true),
    CRASH_APP("testsetting.crashapp", "Crash app", "Force java crash next app startup"),
    DEBUG_FIREBASE("testsetting.debugfirebase", "Enable Firebase remote config (DEBUG Builds)", "Enable reading feature flag from Firebase on debug builds", false)
}
