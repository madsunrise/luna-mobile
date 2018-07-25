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
            id = client.id().toLong(),
            name = client.name(),
            user = User(client.user()),
            avatar = client.avatar()?.let { Photo(it) },
            favorites = ArrayList<Master>() // TODO пока не грузим это
    )

    constructor(client: MasterQuery.Client1) : this(
            id = client.id().toLong(),
            name = client.name(),
            user = User(client.user()),
            avatar = client.avatar()?.let { Photo(it) },
            favorites = ArrayList<Master>() // TODO пока не грузим это
    )

    constructor(client: MasterQuery.Client2) : this(
            id = client.id().toLong(),
            name = client.name(),
            user = User(client.user()),
            avatar = client.avatar()?.let { Photo(it) },
            favorites = ArrayList<Master>() // TODO пока не грузим это
    )
}