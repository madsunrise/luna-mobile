package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * Класс представляет собой всю услугу полностью (маникюр) и включает основные опции (взаимозаключащие) и дополнительные (по желанию)
 */
@Parcelize
data class Service(
        val id: Long,
        val type: ServiceType,
        val description: String,
        val price: BigDecimal,
        val duration: Int,
        val materials: ArrayList<Material>,
        val photos: ArrayList<Photo>,
        val ctime: Date
) : Parcelable {

    constructor(service: MasterQuery.Service) : this(
            id = service.id().toLong(),
            type = ServiceType(service.type()),
            description = service.description(),
            price = service.price() as BigDecimal,
            duration = service.duration(),
            materials = ArrayList<Material>().apply { addAll(service.materials().map { Material(it) }) },
            photos = ArrayList<Photo>().apply { addAll(service.photos().map { Photo(it) }) },
            ctime = Date(service.ctime().toLong())
    )

    constructor(service: MasterQuery.Service1) : this(
            id = service.id().toLong(),
            type = ServiceType(service.type()),
            description = service.description(),
            price = service.price() as BigDecimal,
            duration = service.duration(),
            materials = ArrayList<Material>().apply { addAll(service.materials().map { Material(it) }) },
            photos = ArrayList<Photo>().apply { addAll(service.photos().map { Photo(it) }) },
            ctime = Date(service.ctime().toLong())
    )

    constructor(service: MasterQuery.Service2) : this(
            id = service.id().toLong(),
            type = ServiceType(service.type()),
            description = service.description(),
            price = service.price() as BigDecimal,
            duration = service.duration(),
            materials = ArrayList<Material>().apply { addAll(service.materials().map { Material(it) }) },
            photos = ArrayList<Photo>().apply { addAll(service.photos().map { Photo(it) }) },
            ctime = Date(service.ctime().toLong())
    )
}