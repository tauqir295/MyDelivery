package com.my.delivery.datasource.remote

import android.content.Context
import com.my.delivery.datasource.AppDataSource
import com.my.delivery.datasource.ApiInterface
import com.my.delivery.datasource.ApiClient
import com.my.delivery.deliverylist.model.Delivery
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRemoteDataSource : AppDataSource {

    /**
     *  Using retrofit 2 to fetch data from API
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

        // initiating ApiInterface via retrofit
        val apiService = ApiClient.getClient()?.create(ApiInterface::class.java)

        // API call to receive data from API using retrofit
        val call = apiService?.getDeliveries(offset, limit)

        //queueing the call to listen the api response
        call?.enqueue(object : Callback<ArrayList<Delivery>> {
            override fun onResponse(call: Call<ArrayList<Delivery>>, response: Response<ArrayList<Delivery>>) {
                if (response.code() == 200) {

                    val deliverList = response.body()
                    deliverList?.let { listener.onSuccess(it) }
                } else {
                    response.errorBody()?.let {
                        listener.onError(response.code().toString(), it.toString())
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Delivery>>, t: Throwable) {
                listener.onError("0",t.printStackTrace().toString()) // passing error code as 0
            }
        })
    }
}