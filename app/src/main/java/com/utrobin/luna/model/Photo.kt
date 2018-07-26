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

    constructor(photo: FeedQuery.Photo) : this(
            id = photo.fragments().allFields().id().toLong(),
            path = photo.fragments().allFields().path(),
            tags = ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FeedQuery.Photo1) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FeedQuery.Avatar) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar1) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar2) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar3) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar4) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo1) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo2) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo3) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo4) : this(photo.fragments().allFields().id().toLong(),
            photo.fragments().allFields().path(), ArrayList()) {
        photo.fragments().allFields().tags().forEach {
            tags.add(Tag(it))
        }
    }
}