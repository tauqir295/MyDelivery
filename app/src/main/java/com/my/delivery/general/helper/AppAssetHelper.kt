package com.my.delivery.general.helper

import android.content.Context
import com.google.gson.Gson
import com.my.delivery.general.entity.config.APIConfiguration
import com.my.delivery.general.entity.config.FeatureConfiguration

class AppAssetHelper (private val context: Context) {

    /**
     *  Using gson library parsing the data as [APIConfiguration]
     *  @params: country - use this to read read file from relevant path
     *
     *  @return:APIConfiguration - parsed data is returned
     */
    fun getAPIConfiguration (country:String):APIConfiguration {
        val fileName = "$country/apiConfig.json"
        return Gson().fromJson(FileHelper.readJSONFromAsset(context, fileName),
            APIConfiguration::class.java)
    }

    /**
     *  Using gson library parsing the data as [FeatureConfiguration]
     *  @params: country - use this to read read file from relevant path
     *
     *  @return:FeatureConfiguration - parsed data is returned
     */
    fun getFeatureConfiguration (country:String):FeatureConfiguration {
        val fileName = "$country/feature.json"
        return Gson().fromJson(FileHelper.readJSONFromAsset(context, fileName),
            FeatureConfiguration::class.java)
    }
}