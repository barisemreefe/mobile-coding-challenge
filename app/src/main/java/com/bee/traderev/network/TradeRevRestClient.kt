package com.bee.traderev.network

import android.content.ContentValues.TAG
import android.util.Log
import com.bee.traderev.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L

object TradeRevRestClient {
    private val logging by lazy {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder().readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor({ chain ->
                    val original = chain.request()

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Bearer " + TOKEN) // todo
                    }
                    val requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + TOKEN) // todo
                            .method(original.method(), original.body())
                    val request = requestBuilder.build()
                    chain.proceed(request)

                }).build()


    }
    private val retrofit by lazy {
        Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
                .build()
    }
    val api by lazy {
        retrofit.create(TradeRevApi::class.java)
    }
}