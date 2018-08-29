package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AdditionalMaster
import com.utrobin.luna.fragment.AdditionalSalon
import com.utrobin.luna.utils.Utils.dateFormatter
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Review about master or salon
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

    constructor(review: AdditionalMaster.LastReview) : this(
            id = review.fragments().fullReview().id().toLong(),
            client = Client(review.fragments().fullReview().client()),
            seance = Seance(review.fragments().fullReview().seance()),
            stars = review.fragments().fullReview().stars() / 10.0,
            message = review.fragments().fullReview().message(),
            date = dateFormatter.parse(review.fragments().fullReview().date())
    )

    constructor(review: AdditionalSalon.LastReview) : this(
            id = review.fragments().fullReview().id().toLong(),
            client = Client(review.fragments().fullReview().client()),
            seance = Seance(review.fragments().fullReview().seance()),
            stars = review.fragments().fullReview().stars() / 10.0,
            message = review.fragments().fullReview().message(),
            date = dateFormatter.parse(review.fragments().fullReview().date())
    )
}