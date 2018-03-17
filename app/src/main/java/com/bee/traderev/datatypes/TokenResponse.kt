package com.bee.traderev.datatypes

import com.squareup.moshi.Json

data class TokenResponse(@Json(name = "access_token") val token : String)