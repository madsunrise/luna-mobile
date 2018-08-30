package com.utrobin.luna.entity

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
        services: ArrayList<Service>,
        val signsTotal: Map<Long, Int>,
        val masters: ArrayList<Master>,
        val lastReviews: ArrayList<Review>
) : FeedItem(id, Companion.Type.SALON, name, avatar,
        address, stars, signs, photos, ratesCount, commentsCount, services)