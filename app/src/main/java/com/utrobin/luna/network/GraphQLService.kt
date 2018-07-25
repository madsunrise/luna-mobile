package com.utrobin.luna.network

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import com.utrobin.luna.network.NetworkModule.Companion.BASE_URL
import com.utrobin.luna.type.CustomType
import okhttp3.OkHttpClient
import java.math.BigDecimal
import kotlin.coroutines.experimental.suspendCoroutine


class GraphQLService {
    val apolloClient: ApolloClient

    init {
        val okHttpClient = OkHttpClient
                .Builder()
                .build()

        val bigDecimalAdapter = object : CustomTypeAdapter<BigDecimal> {
            override fun decode(value: CustomTypeValue<*>): BigDecimal {
                return BigDecimal(value.value.toString())
            }

            override fun encode(value: BigDecimal): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLString(value.toPlainString())
            }
        }

        apolloClient = ApolloClient.builder()
                .serverUrl(GRAPHQL_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.BIGDECIMAL, bigDecimalAdapter)
                .build()
    }

    suspend fun <T> execute(call: ApolloCall<T>): T = suspendCoroutine { cont ->
        call.enqueue(object : ApolloCall.Callback<T>() {
            override fun onResponse(dataResponse: Response<T>) {
                dataResponse.data()
                        ?.let { cont.resume(it) }
                        ?: cont.resumeWithException(NullPointerException("Response data is null"))
            }

            override fun onFailure(e: ApolloException) {
                cont.resumeWithException(e)
            }
        })
    }

    companion object {
        private const val GRAPHQL_URL = BASE_URL + "graphql"
    }
}