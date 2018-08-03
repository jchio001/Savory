package com.savory.api.models

import com.squareup.moshi.JsonClass

/**
 * Auth token received from our servers.
 */
@JsonClass(generateAdapter = true)
data class SavoryToken(val token : String)