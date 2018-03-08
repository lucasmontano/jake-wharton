package com.lucasmontano.jakewharton.networking

import com.google.gson.GsonBuilder
import com.lucasmontano.jakewharton.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RestAdapterFactory {

    val adapter: Retrofit get() = createAdapter()

    private fun createAdapter(): Retrofit {
        val gson = GsonBuilder().create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URI)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

    }
}