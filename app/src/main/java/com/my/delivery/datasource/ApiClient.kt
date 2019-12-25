package com.my.delivery.datasource

import com.my.delivery.general.manager.ConfigurationManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        private var retrofit: Retrofit? = null

        /**
         * creating the [Retrofit] object using [Retrofit.Builder]
         * baseUrl - based on entity and build flavor
         * @return:Retrofit - use this returned class to call API
         */
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                ConfigurationManager.instance.apiConfiguration?.host?.let {
                    retrofit = Retrofit.Builder()
                        .baseUrl(it)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }

            }
            return this.retrofit
        }
    }

}