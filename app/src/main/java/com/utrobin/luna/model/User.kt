package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
        val id: Long,
        val email: String,
        val password: String?,
        val role: Int,
        val ctime: Date
) : Parcelable {

    constructor(user: MasterQuery.User) : this(
            id = user.fragments().userWithoutPassword().id().toLong(),
            email = user.fragments().userWithoutPassword().email(),
            password = null,
            role = user.fragments().userWithoutPassword().role(),
            ctime = Date(user.fragments().userWithoutPassword().ctime().toLong())
    )

    constructor(user: MasterQuery.User1) : this(
            id = user.fragments().userWithoutPassword().id().toLong(),
            email = user.fragments().userWithoutPassword().email(),
            password = null,
            role = user.fragments().userWithoutPassword().role(),
            ctime = Date(user.fragments().userWithoutPassword().ctime().toLong())
    )

    constructor(user: MasterQuery.User2) : this(
            id = user.fragments().userWithoutPassword().id().toLong(),
            email = user.fragments().userWithoutPassword().email(),
            password = null,
            role = user.fragments().userWithoutPassword().role(),
            ctime = Date(user.fragments().userWithoutPassword().ctime().toLong())
    )

    constructor(user: MasterQuery.User3) : this(
            id = user.fragments().userWithoutPassword().id().toLong(),
            email = user.fragments().userWithoutPassword().email(),
            password = null,
            role = user.fragments().userWithoutPassword().role(),
            ctime = Date(user.fragments().userWithoutPassword().ctime().toLong())
    )

    constructor(user: MasterQuery.User4) : this(
            id = user.fragments().userWithoutPassword().id().toLong(),
            email = user.fragments().userWithoutPassword().email(),
            password = null,
            role = user.fragments().userWithoutPassword().role(),
            ctime = Date(user.fragments().userWithoutPassword().ctime().toLong())
    )
}