package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable
import com.utrobin.luna.FeedQuery

/**
 * Created by ivan on 02.01.2018.
 */

data class Photo(val path: String, val tags: ArrayList<Tag> = ArrayList()) : Parcelable {
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

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(path)
        parcel.writeTypedList(tags)
    }

    override fun describeContents() = 0


    constructor(parcel: Parcel) : this(parcel.readString()) {
        parcel.readTypedList(tags, Tag.CREATOR)
    }

    companion object {
        val CREATOR = object : Parcelable.Creator<Photo> {
            override fun createFromParcel(`in`: Parcel): Photo {
                return Photo(`in`)
            }

            override fun newArray(size: Int): Array<Photo?> {
                return arrayOfNulls(size)
            }
        }
    }
}