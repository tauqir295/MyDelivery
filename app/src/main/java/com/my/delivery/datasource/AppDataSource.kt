package com.my.delivery.datasource

import android.content.Context

interface AppDataSource {
    fun getDeliveryList(
        offset: Int,
        limit: Int,
        listener: DataSourceListener,
        applicationContext: Context
    )

    interface DataSourceListener {
        fun onSuccess(data: Any)
        fun onError(errorCode: String, errorMessage: String)
    }
}