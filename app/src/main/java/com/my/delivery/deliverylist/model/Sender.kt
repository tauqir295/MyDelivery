package com.my.delivery.deliverylist.model

import androidx.room.Entity
import java.io.Serializable

@Entity
data class Sender(
    val email: String,
    val name: String,
    val phone: String
): Serializable