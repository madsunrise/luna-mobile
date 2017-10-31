package com.utrobin.luna.network

import dagger.Module
import dagger.Provides

/**
 * Created by ivan on 31.10.2017.
 */

@Module
class NetworkModule {
    @Provides
    internal fun provideNetworkService(): NetworkService {
        return NetworkService()
    }
}