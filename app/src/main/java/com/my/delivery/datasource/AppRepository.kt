package com.my.delivery.datasource

import android.annotation.SuppressLint
import android.content.Context

class AppRepository(private val dataSource: AppDataSource) : AppDataSource {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: AppRepository? = null

        /**
         * create new instance
         */
        fun getInstance(dataSource: AppDataSource) =
            INSTANCE ?: synchronized(AppRepository::class.java) {
                INSTANCE ?: AppRepository(dataSource)
                    .also { INSTANCE = it }
            }

        /**
         * destroy the instance
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

    // middle layer method to pass on listener to remote/local data source from fragment/activity
    override fun getDeliveryList(
        offset: Int,
        limit: Int,
        listener: AppDataSource.DataSourceListener,
        applicationContext: Context
    ) {
        dataSource.getDeliveryList(offset, limit, listener, applicationContext)
    }
}