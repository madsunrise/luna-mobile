package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Отзыв о мастере
 */
@Parcelize
data class Review(
        val id: Long,
        val client: Client,
        val seance: Seance,
        val stars: Double,
        val message: String?,
        val date: Date
) : Parcelable {

    constructor(review: MasterQuery.LastReview) : this(
            id = review.fragments().fullReview().id().toLong(),
            client = Client(review.fragments().fullReview().client()),
            seance = Seance(review.fragments().fullReview().seance()),
            stars = review.fragments().fullReview().stars() / 10.0,
            message = review.fragments().fullReview().message(),
            date = Date(review.fragments().fullReview().date().toLong())
    )

    constructor(review: MasterQuery.LastReview1) : this(
            id = review.fragments().fullReview().id().toLong(),
            client = Client(review.fragments().fullReview().client()),
            seance = Seance(review.fragments().fullReview().seance()),
            stars = review.fragments().fullReview().stars() / 10.0,
            message = review.fragments().fullReview().message(),
            date = Date(review.fragments().fullReview().date().toLong())
    )

    constructor(review: MasterQuery.LastReview2) : this(
            id = review.fragments().fullReview().id().toLong(),
            client = Client(review.fragments().fullReview().client()),
            seance = Seance(review.fragments().fullReview().seance()),
            stars = review.fragments().fullReview().stars() / 10.0,
            message = review.fragments().fullReview().message(),
            date = Date(review.fragments().fullReview().date().toLong())
    )
}