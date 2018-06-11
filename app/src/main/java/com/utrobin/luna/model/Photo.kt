package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import kotlinx.android.parcel.Parcelize

/**
 * Created by ivan on 02.01.2018.
 */
@Parcelize
data class Photo(val path: String, val tags: ArrayList<Tag> = ArrayList()) : Parcelable {
    constructor(photo: FeedQuery.Photo) : this(photo.path()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FeedQuery.Avatar) : this(photo.path()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }
}