package com.utrobin.luna.entity

import com.utrobin.luna.fragment.AdditionalSalon


class Master(
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
        val user: User?,
        val salon: Salon?,
        val schedules: ArrayList<Schedule>,
        val seances: ArrayList<Seance>,
        val lastReviews: ArrayList<Review>
) : FeedItem(id, Companion.Type.MASTER, name, avatar,
        address, stars, signs, photos, ratesCount, commentsCount, services) {

    constructor(salon: Salon, master: AdditionalSalon.Master) : this(
            id = master.fragments().masterInsideSalon().id().toLong(),
            name = master.fragments().masterInsideSalon().name(),
            avatar = master.fragments().masterInsideSalon().avatar()?.let { Photo(it) },
            address = null,
            stars = master.fragments().masterInsideSalon().stars() / 10.0,
            signs = ArrayList<Sign>(),
            photos = ArrayList<Photo>(),
            ratesCount = master.fragments().masterInsideSalon().ratesCount(),
            commentsCount = 0,
            services = ArrayList<Service>().apply {
                addAll(master.fragments().masterInsideSalon().services().map { Service(it) })
            },
            user = null,
            salon = salon,
            schedules = ArrayList<Schedule>(),
            seances = ArrayList<Seance>(),
            lastReviews = ArrayList<Review>()
    )
}