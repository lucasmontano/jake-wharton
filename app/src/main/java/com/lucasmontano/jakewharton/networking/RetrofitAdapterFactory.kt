package com.lucasmontano.jakewharton.networking

import com.google.gson.GsonBuilder
import com.lucasmontano.jakewharton.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.lucasmontano.jakewharton.data.ResponseData
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.lucasmontano.jakewharton.data.ErrorData
import com.lucasmontano.jakewharton.data.RepoData
import java.lang.reflect.Type


object RetrofitAdapterFactory {

    val adapter: Retrofit get() = createAdapter()

    private fun createAdapter(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val gson = GsonBuilder().registerTypeAdapter(
            ResponseData::class.java, ResponseDeserializer()
        ).create()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
    }

    private class ResponseDeserializer : JsonDeserializer<ResponseData> {

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ResponseData {

            val response = ResponseData(null, null)

            if (json.isJsonArray) {
                // It is an array, parse the RepoData.
                val responseType = object : TypeToken<List<RepoData>>() {}.type
                response.repos = context.deserialize(json, responseType)
            } else {
                // Not an array, parse out the error info.
                val responseType = object : TypeToken<ErrorData>() {}.type
                response.error = context.deserialize(json, responseType)
            }
            return response
        }
    }
}