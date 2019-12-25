package com.my.delivery

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.delivery.datasource.AppRepository
import com.my.delivery.deliverylist.DeliveryListViewModel
import java.lang.IllegalArgumentException

/**
 * Custom factory class
 */
class AppViewModelFactory private constructor(
    private val repository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(DeliveryListViewModel::class.java) -> DeliveryListViewModel(
                    repository
                )
                else -> throw IllegalArgumentException("Unknown type. Can not be instantiated")
            }
        } as T


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: AppViewModelFactory? = null

        fun getInstance(repository: AppRepository) =
            INSTANCE ?: synchronized(DeliveryListViewModel::class.java) {
                INSTANCE ?: AppViewModelFactory(repository)
                    .also { INSTANCE = it }
            }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}