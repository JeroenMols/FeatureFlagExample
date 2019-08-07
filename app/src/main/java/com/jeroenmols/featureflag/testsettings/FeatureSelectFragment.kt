package com.jeroenmols.featureflag.testsettings

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeroenmols.featureflag.example.R
import com.jeroenmols.featureflag.framework.*
import kotlinx.android.synthetic.main.fragment_featureflag.*
import kotlin.system.exitProcess

internal class FeatureSelectFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_featureflag, container, false)

    override fun onResume() {
        super.onResume()
        initializeRecyclerView()
        activity?.title = if (useTestSettings()) "Test (automation) settings" else "Feature Flags"
    }

    private fun initializeRecyclerView() {
        val runtimeFeatureFlagProvider = RuntimeFeatureFlagProvider(context!!)
        val checkedListener = { feature: Feature, enabled: Boolean ->
            runtimeFeatureFlagProvider.setFeatureEnabled(feature, enabled)
            requestRestart()
        }
        if (useTestSettings()) {
            recyclerview_featureflag.adapter =
                FeatureFlagAdapter(
                    TestSetting.values(),
                    runtimeFeatureFlagProvider,
                    checkedListener
                )
        } else {
            recyclerview_featureflag.adapter =
                FeatureFlagAdapter(
                    FeatureFlag.values(),
                    runtimeFeatureFlagProvider,
                    checkedListener
                )
        }
        recyclerview_featureflag.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    private fun useTestSettings() = arguments?.getBoolean(USE_TEST_SETTINGS, false) == true

    companion object {
        fun getInstance(showTestSettings: Boolean): FeatureSelectFragment {
            val args = Bundle().apply { putBoolean(USE_TEST_SETTINGS, showTestSettings) }
            return FeatureSelectFragment().apply { arguments = args }
        }

        private const val USE_TEST_SETTINGS = "useTestSettings"
    }

    private fun requestRestart() {
        val msg = "In order for changes to reflect please restart the app via settings"
        val snackbar = Snackbar.make(view!!, msg, Snackbar.LENGTH_INDEFINITE)
            .setActionTextColor(Color.RED)
            .setAction("Force Stop") {
                exitProcess(-1)
            }
        snackbar.view.setBackgroundColor(Color.BLACK)
        snackbar.show()
    }
}

private class FeatureFlagAdapter<T : Feature>(
    val items: Array<T>,
    val provider: FeatureFlagProvider,
    val checkedListener: Function2<Feature, Boolean, Unit>
) : RecyclerView.Adapter<FeatureFlagViewHolder<T>>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FeatureFlagViewHolder<T>, position: Int) =
        holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureFlagViewHolder<T> {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_featureflag, parent, false)
        return FeatureFlagViewHolder(itemView, provider, checkedListener)
    }
}

private class FeatureFlagViewHolder<T : Feature>(
    view: View,
    val provider: FeatureFlagProvider,
    val checkedListener: Function2<Feature, Boolean, Unit>
) : RecyclerView.ViewHolder(view) {

    fun bind(feature: T) {
        itemView.findViewById<TextView>(R.id.textview_featureflag_title).text = feature.title
        itemView.findViewById<TextView>(R.id.textview_featureflag_description).text =
            feature.explanation
        itemView.findViewById<SwitchCompat>(R.id.switch_featureflag).isChecked =
            provider.isFeatureEnabled(feature)
        itemView.findViewById<SwitchCompat>(R.id.switch_featureflag)
            .setOnCheckedChangeListener { switch, isChecked ->
                if (switch.isPressed) checkedListener.invoke(feature, isChecked)
            }
    }
}