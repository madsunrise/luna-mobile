package com.utrobin.luna.network

import com.apollographql.apollo.ApolloClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import com.utrobin.luna.network.NetworkModule.Companion.BASE_URL
import okhttp3.*
import okio.Buffer

/**
 * Created by ivan on 31.10.2017.
 */

class GraphQLService() {
    val apolloClient: ApolloClient

    private val apolloRequestInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        val originalJson = JsonParser().parse(originalRequest.bodyToString())

        var queryString = originalJson.asJsonObject.get("query").asString
        val vars = originalJson.asJsonObject.get("variables")

        queryString = '{' + queryString.substringAfter('{')
        for (variable in vars.asJsonObject.entrySet()) {
            queryString = queryString.replace("$" + variable.key, " " + variable.value.asString, true)
        }

        originalJson.asJsonObject.addProperty("query", queryString)
        originalJson.asJsonObject.remove("variables")
        val requestString = gson.toJson(originalJson)

        val newRequest = Request
                .Builder()
                .url(originalRequest.url())
                .post(RequestBody.create(originalRequest.body()?.contentType(), requestString))
                .build()


        val originalResponse = chain.proceed(newRequest)

        if (!originalResponse.isSuccessful) {
            return@Interceptor originalResponse // just forwarding
        }

        originalResponse.body()?.let { body ->
            val json = originalResponse.body()?.string()

            if (gson.fromJson<ResponseCode>(json, ResponseCode::class.java).code != 200) {
                return@Interceptor originalResponse  // just forwarding
            }

            val model = gson.fromJson<TransformedResponse>(json, TransformedResponse::class.java)
            val newJson = gson.toJson(model)

            val contentType = body.contentType()
            val newBody = ResponseBody.create(contentType, newJson)
            originalResponse.newBuilder().body(newBody).build()
        } ?: throw NotImplementedError()
    }

    inner class ResponseCode {
        @SerializedName("code")
        var code: Int = 0
    }

    inner class TransformedResponse {
        @SerializedName(value = "data", alternate = arrayOf("body"))
        lateinit var data: JsonElement
    }

    inner class Variables {
        @SerializedName("variables")
        lateinit var variables: List<String>
    }

    private fun Request.bodyToString(): String {
        val copy = this.newBuilder().build()
        val buffer = Buffer()
        copy.body()?.writeTo(buffer)
        return buffer.readUtf8()
    }

    init {
        val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(apolloRequestInterceptor)
                .build()
        apolloClient = ApolloClient.builder()
                .serverUrl(GRAPHQL_URL)
                .okHttpClient(okHttpClient)
                .build();
    }

    companion object {
        private const val GRAPHQL_URL = BASE_URL + "graphql"
        private val gson = Gson()
    }
}