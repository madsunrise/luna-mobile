package com.utrobin.luna.model

import com.utrobin.luna.MasterQuery

class Salon(
        id: Long,
        name: String?,
        avatar: Photo?,
        address: Address?,
        stars: Double,
        signs: ArrayList<Sign>,
        photos: ArrayList<Photo>,
        ratesCount: Int,
        commentsCount: Int,
        val masters: ArrayList<Master>,
        val signsTotal: Int,
        val services: ArrayList<Service>,
        val lastReviews: ArrayList<Review>
) : FeedItem(id, Companion.Type.SALON, name, avatar,
        address, stars, signs, photos, ratesCount, commentsCount) {
    constructor(salon: MasterQuery.Salon) : this(
            id = salon.id().toLong(),
            name = salon.name(),
            avatar = Photo(salon.avatar()),
            address = salon.address()?.let { Address(it) },
            stars = salon.stars() / 10.0,
            signs = ArrayList(),
            photos = ArrayList(),
            ratesCount = salon.ratesCount(),
            commentsCount = salon.commentsCount(),
            masters = ArrayList(),
            signsTotal = salon.signs_total().toInt(),
            services = ArrayList(),
            lastReviews = ArrayList()
    ) {
        signs.addAll(salon.signs().map { Sign(it) })
        photos.addAll(salon.photos().map { Photo(it) })
        masters.addAll(salon.masters().map { Master(it) })
        services.addAll(salon.services().map { Service(it) })
        lastReviews.addAll(salon.lastReviews().map { Review(it) })
    }
}