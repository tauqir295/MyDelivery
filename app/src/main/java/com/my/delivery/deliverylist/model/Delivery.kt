package com.my.delivery.deliverylist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.my.delivery.deliverylist.storage.Converters
import java.io.Serializable

@Entity (tableName = "delivery")
data class Delivery(
    val deliveryFee: String,
    val goodsPicture: String,
    @PrimaryKey val id: String,
    val pickupTime: String,
    val remarks: String,
    @TypeConverters (Converters::class) val route: Route,
    @TypeConverters (Converters::class) val sender: Sender,
    val surcharge: String,
    var isFavorite: Boolean = false
): Serializable