package com.my.delivery.datasource.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.my.delivery.R
import com.my.delivery.datasource.AppDataSource
import com.my.delivery.deliverylist.model.Delivery
import com.my.delivery.general.helper.FileHelper

class AppLocalDataSource : AppDataSource {

    /**
     *  Using Gson library to parse data from local file
     *  @params: offset - starting point for API get query parameter
     *        : limit - end point for API get query parameter
     *        : listener - injecting data source listener
     *        : applicationContext - passing the context
     */
    override fun getDeliveryList(
        offset: Int,
        limit: Int,
        listener: AppDataSource.DataSourceListener,
        applicationContext: Context
    ) {
        val fileName = applicationContext.getString(R.string.stub_file_name)
        val jsonStr = FileHelper.readJSONFromAsset(applicationContext, fileName)
        val typeToken = object : TypeToken<ArrayList<Delivery>>() {}.type
        val data = Gson().fromJson<ArrayList<Delivery>>(jsonStr, typeToken)

        listener.onSuccess(data) // pass the data from local file to listening class
    }
}