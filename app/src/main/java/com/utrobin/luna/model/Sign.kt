package com.utrobin.luna.model

import com.utrobin.luna.FeedQuery

/**
 * Created by ivan on 02.01.2018.
 */

data class Sign(val name: String, val description: String, val photo: Photo) {
    constructor(sign: FeedQuery.Sign) : this(sign.name()!!, sign.description()!!, Photo(sign.photo()!!))
}