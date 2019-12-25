package com.my.delivery.general.helper

import android.content.Context
import com.my.delivery.utils.Logger
import java.io.IOException
import java.io.InputStream

class FileHelper {
    companion object {
        private val TAG = FileHelper::class.java.simpleName

        /**
         *  Using inputStream to read local file
         *  @params: context - passing the context
         *        : fullPath - folder path of the file
         *
         *  @return:String - data read from local file is returned as string
         */
        fun readJSONFromAsset(context: Context, fullPath:String): String? {
            val json: String?
            try {
                val  inputStream: InputStream = context.assets.open(fullPath)
                json = inputStream.bufferedReader().use{
                    it.readText()
                }
            } catch (ex: IOException) {
                Logger.e(TAG, ex.printStackTrace().toString())
                return null
            }
            return json
        }
    }
}