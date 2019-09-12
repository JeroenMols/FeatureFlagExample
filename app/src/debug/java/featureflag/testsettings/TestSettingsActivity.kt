package com.jeroenmols.featureflag.testsettings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeroenmols.featureflag.example.R

private const val TAG_TEST_SETTING = "tag_testsettingfragment"

class TestSettingsActivity : AppCompatActivity(), TestSettingsFragment.TestSettingsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testsettings)

        if (supportFragmentManager.findFragmentByTag(TAG_TEST_SETTING) == null) {
            val settingsFragment = TestSettingsFragment().apply { testSettingListener = this@TestSettingsActivity }
            supportFragmentManager.beginTransaction()
                .replace(R.id.framelayout_testsettings_fragmentcontainer, settingsFragment).commit()
        } else {
            (supportFragmentManager.findFragmentByTag(TAG_TEST_SETTING) as TestSettingsFragment).testSettingListener =
                this
        }
    }

    override fun onFeatureToggleClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout_testsettings_fragmentcontainer, FeatureSelectFragment.getInstance(false))
            .addToBackStack(null).commit()
    }

    override fun onTestSettingClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout_testsettings_fragmentcontainer, FeatureSelectFragment.getInstance(true))
            .addToBackStack(null).commit()
    }
}
