package com.utrobin.luna.model

import com.apollographql.apollo.ApolloCall
import com.utrobin.luna.network.GraphQLService

class ExecuteApolloCallJob<T>(
        private val graphQLService: GraphQLService,
        private val call: ApolloCall<T>
) : UseCase<T>() {

    override suspend fun executeOnBackground(): T {
        return graphQLService.execute(call)
    }
}