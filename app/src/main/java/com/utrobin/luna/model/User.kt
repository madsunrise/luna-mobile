package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AdditionalMaster
import com.utrobin.luna.fragment.BaseClient
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

    constructor(user: AdditionalMaster.User) : this(
            id = user.fragments().userWithoutPassword().id().toLong(),
            email = user.fragments().userWithoutPassword().email(),
            password = null,
            role = user.fragments().userWithoutPassword().role(),
            ctime = Date(user.fragments().userWithoutPassword().ctime().toLong())
    )

    constructor(user: AdditionalMaster.User1) : this(
            id = user.fragments().userWithoutPassword().id().toLong(),
            email = user.fragments().userWithoutPassword().email(),
            password = null,
            role = user.fragments().userWithoutPassword().role(),
            ctime = Date(user.fragments().userWithoutPassword().ctime().toLong())
    )

    constructor(user: BaseClient.User) : this(
            id = user.fragments().userWithoutPassword().id().toLong(),
            email = user.fragments().userWithoutPassword().email(),
            password = null,
            role = user.fragments().userWithoutPassword().role(),
            ctime = Date(user.fragments().userWithoutPassword().ctime().toLong())
    )
}