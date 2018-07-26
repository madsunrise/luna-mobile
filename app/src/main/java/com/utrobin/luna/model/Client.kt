package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Client(
        val id: Long,
        val name: String?,
        val user: User,
        val avatar: Photo?,
        val favorites: ArrayList<Master>
) : Parcelable {

    constructor(client: MasterQuery.Client) : this(
            id = client.fragments().baseClient().id().toLong(),
            name = client.fragments().baseClient().name(),
            user = User(client.fragments().baseClient().user()),
            avatar = client.fragments().baseClient().avatar()?.let { Photo(it) },
            favorites = ArrayList<Master>() // TODO пока не грузим это
    )

    constructor(client: MasterQuery.Client1) : this(
            id = client.fragments().baseClient().id().toLong(),
            name = client.fragments().baseClient().name(),
            user = User(client.fragments().baseClient().user()),
            avatar = client.fragments().baseClient().avatar()?.let { Photo(it) },
            favorites = ArrayList<Master>() // TODO пока не грузим это
    )

    constructor(client: MasterQuery.Client2) : this(
            id = client.fragments().baseClient().id().toLong(),
            name = client.fragments().baseClient().name(),
            user = User(client.fragments().baseClient().user()),
            avatar = client.fragments().baseClient().avatar()?.let { Photo(it) },
            favorites = ArrayList<Master>() // TODO пока не грузим это
    )
}