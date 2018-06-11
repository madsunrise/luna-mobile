package com.utrobin.luna.network

import com.apollographql.apollo.ApolloClient
import com.utrobin.luna.network.NetworkModule.Companion.BASE_URL
import okhttp3.OkHttpClient

class GraphQLService {
    val apolloClient: ApolloClient

    init {
        val okHttpClient = OkHttpClient
                .Builder()
                .build()
        apolloClient = ApolloClient.builder()
                .serverUrl(GRAPHQL_URL)
                .okHttpClient(okHttpClient)
                .build()
    }

    companion object {
        private const val GRAPHQL_URL = BASE_URL + "graphql"
    }
}