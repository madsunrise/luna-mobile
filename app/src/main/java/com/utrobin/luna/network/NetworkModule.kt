package com.utrobin.luna.network

import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideGraphQLService(): GraphQLService {
        return GraphQLService()
    }

    companion object {
        const val BASE_URL = "https://utrobin.com/api/"
    }
}