package com.utrobin.luna.entity

import android.os.Parcelable
import com.utrobin.luna.fragment.FullReview
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Client(
        val id: Long,
        val name: String?,
        val user: User,
        val avatar: Photo?,
        val favorites: ArrayList<Master>
) : Parcelable {

    constructor(client: FullReview.Client) : this(
            id = client.fragments().baseClient().id().toLong(),
            name = client.fragments().baseClient().name(),
            user = User(client.fragments().baseClient().user()),
            avatar = client.fragments().baseClient().avatar()?.let { Photo(it) },
            favorites = ArrayList<Master>()
    )
}