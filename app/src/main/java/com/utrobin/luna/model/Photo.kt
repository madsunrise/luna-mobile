package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.MasterQuery
import com.utrobin.luna.fragment.FullService
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        val id: Long,
        val path: String,
        val tags: ArrayList<Tag>) : Parcelable {

    constructor(photo: FeedQuery.Photo) : this(
            id = photo.fragments().fullPhoto().id().toLong(),
            path = photo.fragments().fullPhoto().path(),
            tags = ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FeedQuery.Avatar) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar1) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar2) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar3) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Avatar4) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterQuery.Photo1) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FullService.Photo) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }
}