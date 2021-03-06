package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FeedItem(
        val id: Long,
        val type: Type,
        val name: String?,
        val avatar: Photo?,
        val address: Address?,
        val stars: Double,
        val signs: ArrayList<Sign>,
        val photos: ArrayList<Photo>,
        val ratesCount: Int,
        val commentsCount: Int
) : Parcelable {
    companion object {
        enum class Type { SALON, MASTER }
    }
}