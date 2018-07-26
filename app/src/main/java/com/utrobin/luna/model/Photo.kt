package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AdditionalMaster
import com.utrobin.luna.fragment.BaseClient
import com.utrobin.luna.fragment.FullFeedItem
import com.utrobin.luna.fragment.FullService
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        val id: Long,
        val path: String,
        val tags: ArrayList<Tag>) : Parcelable {

    constructor(photo: FullFeedItem.Photo) : this(
            id = photo.fragments().fullPhoto().id().toLong(),
            path = photo.fragments().fullPhoto().path(),
            tags = ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FullFeedItem.Avatar) : this(
            id = photo.fragments().fullPhoto().id().toLong(),
            path = photo.fragments().fullPhoto().path(),
            tags = ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: AdditionalMaster.Avatar) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: AdditionalMaster.Avatar1) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: AdditionalMaster.Photo) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: AdditionalMaster.Photo1) : this(photo.fragments().fullPhoto().id().toLong(),
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

    constructor(photo: BaseClient.Avatar) : this(photo.fragments().fullPhoto().id().toLong(),
            photo.fragments().fullPhoto().path(), ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }
}