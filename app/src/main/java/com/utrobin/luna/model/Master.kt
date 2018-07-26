package com.utrobin.luna.model

import com.utrobin.luna.fragment.AdditionalMaster
import java.util.*


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
        val user: User,
        val salon: Salon?,
        val services: ArrayList<Service>,
        val schedules: ArrayList<Schedule>,
        val seances: ArrayList<Seance>,
        val lastReviews: ArrayList<Review>
) : FeedItem(id, Companion.Type.MASTER, name, avatar,
        address, stars, signs, photos, ratesCount, commentsCount) {

    constructor(master: AdditionalMaster.Master) : this(null, master)

    // Конструктор для мастера, который вызывается при создании САЛОНА
    constructor(salon: Salon?, master: AdditionalMaster.Master) : this(
            id = master.id().toLong(),
            name = master.name(),
            avatar = master.avatar()?.let { Photo(it) },
            address = master.address()?.let { Address(it) },
            stars = master.stars() / 10.0,
            signs = ArrayList(),
            photos = ArrayList(),
            ratesCount = master.ratesCount(),
            commentsCount = master.commentsCount(),
            user = User(master.user()),
            salon = salon,
            services = ArrayList<Service>().apply { addAll(master.services().map { Service(it) }) },
            schedules = ArrayList<Schedule>().apply { addAll(master.schedules().map { Schedule(it) }) },
            seances = ArrayList<Seance>().apply { addAll(master.seances().map { Seance(it) }) },
            lastReviews = ArrayList<Review>().apply { addAll(master.lastReviews().map { Review(it) }) }
    )
}