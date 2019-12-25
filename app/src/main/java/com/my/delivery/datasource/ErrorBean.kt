package com.my.delivery.datasource

import java.io.Serializable

/**
 * cutom class to pass the error object in case API call fails
 */
data class ErrorBean (
    val errorCode: String,
    val errorMessage: String
): Serializable