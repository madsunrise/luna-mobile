package com.utrobin.luna.model

import com.utrobin.luna.FeedQuery

/**
 * Created by ivan on 02.01.2018.
 */

data class Tag(val name: String) {
    constructor(tag: FeedQuery.Tag) : this(tag.name()!!)
    constructor(tag: FeedQuery.Tag1) : this(tag.name()!!)
}