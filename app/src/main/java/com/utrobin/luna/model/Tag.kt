package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable
import com.utrobin.luna.FeedQuery

/**
 * Created by ivan on 02.01.2018.
 */

data class Tag(val name: String) : Parcelable {
    constructor(tag: FeedQuery.Tag) : this(tag.name()!!)
    constructor(tag: FeedQuery.Tag1) : this(tag.name()!!)

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(name)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(parcel.readString())

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Tag> {
            override fun createFromParcel(`in`: Parcel): Tag {
                return Tag(`in`)
            }

            override fun newArray(size: Int): Array<Tag?> {
                return arrayOfNulls(size)
            }
        }
    }
}