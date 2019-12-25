package com.my.delivery.deliverylist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my.delivery.datasource.AppDataSource
import com.my.delivery.datasource.AppRepository
import com.my.delivery.datasource.ErrorBean
import com.my.delivery.deliverylist.model.Delivery

/**
 * Custom ViewModel class
 */
class DeliveryListViewModel(private val repository: AppRepository) : ViewModel() {

    private val _deliveryList: MutableLiveData<ArrayList<Delivery>> = MutableLiveData()
    val deliveryList: LiveData<ArrayList<Delivery>> = _deliveryList
    private val inProgress = MutableLiveData<Boolean>()
    val errorBean : MutableLiveData<ErrorBean> = MutableLiveData()

    /**
     *  Using repository class listen to data from local/remote data source
     *  @params: offset - starting point for API get query parameter
     *        : limit - end point for API get query parameter
     *        : applicationContext - passing the context
     *
     *  @return:LiveData<ArrayList<Delivery>> - use this list to populate the data
     */
    fun getDeliveryList(
        offset: Int,
        limit: Int,
        applicationContext: Context
    ): LiveData<ArrayList<Delivery>> {
        inProgress.value = true

        // call the data from local/remote data source
        repository.getDeliveryList(offset, limit, object : AppDataSource.DataSourceListener {
            override fun onSuccess(data: Any) {
                inProgress.value = false
                _deliveryList.postValue(data as ArrayList<Delivery>)
            }

            override fun onError(errorCode: String, errorMessage: String) {
                inProgress.value = false
                errorBean.postValue(ErrorBean(errorCode, errorMessage)) // creating custom error bean class
            }
        }, applicationContext)

        // Return immutable list of deliveries
        return deliveryList
    }
}