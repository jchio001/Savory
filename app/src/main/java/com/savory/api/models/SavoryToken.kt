package com.savory.api.models

import com.squareup.moshi.JsonClass

/**
 * Auth token receievd from our servers.
 */
@JsonClass(generateAdapter = true)
data class SavoryToken(val token : String)