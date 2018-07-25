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


    constructor(user: MasterQuery.User) : this(user.id().toLong(),
            user.email(), null, user.role(), Date(user.ctime().toLong()))

    constructor(user: MasterQuery.User1) : this(user.id().toLong(),
            user.email(), null, user.role(), Date(user.ctime().toLong()))

    constructor(user: MasterQuery.User2) : this(user.id().toLong(),
            user.email(), null, user.role(), Date(user.ctime().toLong()))

    constructor(user: MasterQuery.User3) : this(user.id().toLong(),
            user.email(), null, user.role(), Date(user.ctime().toLong()))

    constructor(user: MasterQuery.User4) : this(user.id().toLong(),
            user.email(), null, user.role(), Date(user.ctime().toLong()))
}