package com.bee.traderev.datatypes

import com.squareup.moshi.Json

data class User(val name: String?,
                @Json(name = "instagram_username") val instagramUserName : String?,
                @Json(name = "twitter_username") val twitterUserName : String?,
                val bio : String?,
                val location : String?,
                @Json(name = "portfolio_url") val portfolioUrl : String?)