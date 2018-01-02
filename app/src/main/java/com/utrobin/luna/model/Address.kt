package com.utrobin.luna.model

import com.utrobin.luna.FeedQuery

/**
 * Created by ivan on 02.01.2018.
 */

data class Address(val description: String, val lat: Double, val lon: Double) {
    constructor(address: FeedQuery.Address) : this(address.description()!!, address.lat()!!, address.lon()!!)
}