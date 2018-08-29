package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.BaseClient
import com.utrobin.luna.fragment.FullFeedItem
import com.utrobin.luna.fragment.FullService
import com.utrobin.luna.fragment.MasterInsideSalon
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        val id: Long,
        val path: String,
        val tags: ArrayList<Tag>) : Parcelable {

    constructor(photo: FullFeedItem.Photo) : this(
            id = photo.fragments().fullPhoto().fragments().basePhoto().id().toLong(),
            path = photo.fragments().fullPhoto().fragments().basePhoto().path(),
            tags = ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: FullFeedItem.Avatar) : this(
            id = photo.fragments().fullPhoto().fragments().basePhoto().id().toLong(),
            path = photo.fragments().fullPhoto().fragments().basePhoto().path(),
            tags = ArrayList()) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: MasterInsideSalon.Avatar) : this(
            id = photo.fragments().basePhoto().id().toLong(),
            path = photo.fragments().basePhoto().path(),
            tags = ArrayList())

    constructor(photo: FullService.Photo) : this(
            id = photo.fragments().fullPhoto().fragments().basePhoto().id().toLong(),
            path = photo.fragments().fullPhoto().fragments().basePhoto().path(),
            tags = ArrayList()
    ) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }

    constructor(photo: BaseClient.Avatar) : this(
            id = photo.fragments().fullPhoto().fragments().basePhoto().id().toLong(),
            path = photo.fragments().fullPhoto().fragments().basePhoto().path(),
            tags = ArrayList()
    ) {
        photo.fragments().fullPhoto().tags().forEach {
            tags.add(Tag(it))
        }
    }
}