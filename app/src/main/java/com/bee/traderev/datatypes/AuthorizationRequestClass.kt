package com.bee.traderev.datatypes

import com.squareup.moshi.Json

data class AuthorizationRequestClass(@Json(name = "client_id") private val clientId : String,
                                     @Json(name = "client_secret")private val secret : String,
                                     @Json(name = "redirect_uri")private val redirectUri : String,
                                     @Json(name = "code")private val authCode : String,
                                     @Json(name = "grant_type")private val grantType : String)