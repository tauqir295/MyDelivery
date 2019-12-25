package com.my.delivery.utils

import android.util.Log
import com.my.delivery.BuildConfig

object Logger {

    /**
     * check debug flavor
     */
    private fun enableLog():Boolean {
        return BuildConfig.DEBUG
    }

    fun d (tag:String, msg:String) {
        when {
            enableLog() -> Log.d(tag, msg)
        }
    }

    fun e (tag:String, msg:String) {
        when {
            enableLog() -> Log.d(tag, msg)
        }
    }
}