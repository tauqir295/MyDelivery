package com.my.delivery.utils

import com.my.delivery.deliverylist.model.Delivery
import java.text.NumberFormat

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UtilityHelper {

    /**
     * Adding the deliveryFee and surcharge
     * calculating the price upto 2 places
     * @param:delivery using this variable to calculate
     *
     * @return:String - returning the formatted price
     */
    fun formattedPrice(delivery: Delivery) : String{
        val format = NumberFormat.getCurrencyInstance()
        val surchargeVal = format.parse(delivery.surcharge)
        val deliveryFeeVal = format.parse(delivery.deliveryFee)

        val addedVal = surchargeVal.toFloat() + deliveryFeeVal.toFloat() //adding surcharge and deliveryFee

        return String.format("$%.2f", addedVal) //formatting the value upto 2 places
    }
}