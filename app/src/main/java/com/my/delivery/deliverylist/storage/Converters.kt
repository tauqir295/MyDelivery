package com.my.delivery.deliverylist.storage

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.my.delivery.deliverylist.model.Route
import com.my.delivery.deliverylist.model.Sender

/**
 * Custom class for type converting the data class
 * Marks a method as a type converter.
 * A class can have as many @TypeConverter methods as it needs.
 */
class Converters {

    /**
     * parsing the json data class
     * @param:json - use it to parse string as [Sender] class
     * @return:Sender - returning the parsed object
     *
     */
    @TypeConverter
    fun toSender(json: String): Sender {
        val type = object : TypeToken<Sender>() {}.type
        return Gson().fromJson(json, type)
    }

    /**
     * type converting the data class to json
     * @param:sender - use it to parse [sender] as json
     * @return:String - return [sender] as json string
     */
    @TypeConverter
    fun toJson(sender: Sender): String {
        val type = object: TypeToken<Sender>() {}.type
        return Gson().toJson(sender, type)
    }

    /**
     * parsing the json data class
     * @param:json - use it to parse string as [Route] class
     * @return:Route - returning the parsed object
     *
     */
    @TypeConverter
    fun toRoute(json: String): Route {
        val type = object : TypeToken<Route>() {}.type
        return Gson().fromJson(json, type)
    }

    /**
     * type converting the data class to json
     * @param:route - use it to parse [route] as json
     * @return:String - return [route] as json string
     */
    @TypeConverter
    fun toJson(route: Route): String {
        val type = object: TypeToken<Route>() {}.type
        return Gson().toJson(route, type)
    }

}