package com.utrobin.luna.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.CustomTypeAdapter
import com.utrobin.luna.network.NetworkModule.Companion.BASE_URL
import com.utrobin.luna.type.CustomType
import okhttp3.OkHttpClient
import java.math.BigDecimal


class GraphQLService {
    val apolloClient: ApolloClient

    init {
        val okHttpClient = OkHttpClient
                .Builder()
                .build()

        val bigDecimalAdapter = object : CustomTypeAdapter<BigDecimal> {
            override fun decode(value: String): BigDecimal {
                return BigDecimal(value)
            }

            override fun encode(value: BigDecimal): String {
                return value.toPlainString()
            }
        }

        apolloClient = ApolloClient.builder()
                .serverUrl(GRAPHQL_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.BIGDECIMAL, bigDecimalAdapter)
                .build()
    }

    companion object {
        private const val GRAPHQL_URL = BASE_URL + "graphql"
    }
}