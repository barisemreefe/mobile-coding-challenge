package com.bee.traderev.network

import com.bee.traderev.datatypes.AuthorizationRequestClass
import com.bee.traderev.datatypes.Photo
import com.bee.traderev.datatypes.TokenResponse
import retrofit2.Call
import retrofit2.http.*

interface TradeRevApi {

    @GET("oauth/authorize/")
    fun authorize(@Query("client_id")clientId : String,@Query("scope")scope : String,
                  @Query("response_type") responseType : String,
                  @Query("redirect_uri") redirectUri : String) : Call<Any>

    @POST
    fun getToken(@Url url : String, @Body authorizationRequestClass: AuthorizationRequestClass) : Call<TokenResponse>

    @GET("/photos/curated/")
    fun getPhotos() : Call<List<Photo>>


}
