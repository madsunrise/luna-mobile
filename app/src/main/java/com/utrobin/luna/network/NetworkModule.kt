package com.utrobin.luna.network

import dagger.Module
import dagger.Provides

/**
 * Created by ivan on 31.10.2017.
 */

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