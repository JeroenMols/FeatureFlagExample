package com.jeroenmols.featureflag.testsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeroenmols.featureflag.example.R
import kotlinx.android.synthetic.main.fragment_testsettings.*

class TestSettingsFragment : androidx.fragment.app.Fragment() {

    interface TestSettingsListener {
        fun onFeatureToggleClicked()
        fun onTestSettingClicked()
    }

    var testSettingListener: TestSettingsListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_testsettings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textview_testsettings_featuretoggle.setOnClickListener { testSettingListener?.onFeatureToggleClicked() }
        formattextview_testsettings_testsetting.setOnClickListener { testSettingListener?.onTestSettingClicked() }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "Test Settings"
    }
}