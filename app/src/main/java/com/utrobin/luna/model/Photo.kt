package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        val id: Long,
        val path: String,
        val tags: ArrayList<Tag>) : Parcelable {

    constructor(photo: FeedQuery.Photo) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FeedQuery.Photo1) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FeedQuery.Avatar) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar1) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar2) : this(photo.id().toLong(), photo.path(), ArrayList())

    constructor(photo: MasterQuery.Avatar3) : this(photo.id().toLong(), photo.path(), ArrayList())

    constructor(photo: MasterQuery.Avatar4) : this(photo.id().toLong(), photo.path(), ArrayList())

    constructor(photo: MasterQuery.Photo) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo1) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo2) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo3) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo4) : this(photo.id().toLong(), photo.path(), ArrayList()) {
        photo.tags().forEach {
            tags.add(Tag(it))
        }
    }
}