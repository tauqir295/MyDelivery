package com.my.delivery.deliverylist.model

import androidx.room.Entity
import java.io.Serializable

@Entity
data class Route(
    val end: String,
    val start: String
): Serializable