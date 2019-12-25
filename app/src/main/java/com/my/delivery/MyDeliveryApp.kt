package com.my.delivery

import android.app.Application
import com.my.delivery.general.helper.AppAssetHelper
import com.my.delivery.general.manager.ConfigurationManager
import com.my.delivery.utils.Logger
import java.io.IOException

class MyDeliveryApp : Application() {
    private val TAG = Application::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        setAppConfiguration()
    }

    /**
     * Use this feature on single code base to maintain
     * multi-environment entities and build flavors
     *
     * use [BuildConfig.FLAVOR_country] to initiate different [apiConfiguration] and [featureConfiguration]
     *
     */
    private fun setAppConfiguration() {
        try {
            ConfigurationManager.instance.apiConfiguration = AppAssetHelper(this).getAPIConfiguration(BuildConfig.FLAVOR_country)
            ConfigurationManager.instance.featureConfiguration = AppAssetHelper(this).getFeatureConfiguration(BuildConfig.FLAVOR_country)

        } catch (e:IOException) {
            e.message?.let { Logger.e(TAG, it) }
        }
    }
}