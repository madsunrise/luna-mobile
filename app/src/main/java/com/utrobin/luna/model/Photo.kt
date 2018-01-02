package com.utrobin.luna.model

import com.utrobin.luna.FeedQuery

/**
 * Created by ivan on 02.01.2018.
 */

data class Photo(val path: String, val tags: ArrayList<Tag> = ArrayList()) {
    constructor(photo: FeedQuery.Photo) : this(photo.path()!!) {
        photo.tags()?.forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FeedQuery.Avatar) : this(photo.path()!!) {
        photo.tags()?.forEach {
            tags.add(Tag(it))
        }
    }
}