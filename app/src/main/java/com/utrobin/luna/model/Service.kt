package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AdditionalMaster
import com.utrobin.luna.utils.Utils.dateFormatter
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

    constructor(service: AdditionalMaster.Service) : this(
            id = service.fragments().fullService().id().toLong(),
            type = ServiceType(service.fragments().fullService().type()),
            description = service.fragments().fullService().description(),
            price = service.fragments().fullService().price() as BigDecimal,
            duration = service.fragments().fullService().duration(),
            materials = ArrayList<Material>().apply { addAll(service.fragments().fullService().materials().map { Material(it) }) },
            photos = ArrayList<Photo>().apply { addAll(service.fragments().fullService().photos().map { Photo(it) }) },
            ctime = dateFormatter.parse(service.fragments().fullService().ctime())
    )

    constructor(service: AdditionalMaster.Service1) : this(
            id = service.fragments().fullService().id().toLong(),
            type = ServiceType(service.fragments().fullService().type()),
            description = service.fragments().fullService().description(),
            price = service.fragments().fullService().price() as BigDecimal,
            duration = service.fragments().fullService().duration(),
            materials = ArrayList<Material>().apply { addAll(service.fragments().fullService().materials().map { Material(it) }) },
            photos = ArrayList<Photo>().apply { addAll(service.fragments().fullService().photos().map { Photo(it) }) },
            ctime = dateFormatter.parse(service.fragments().fullService().ctime())
    )

    constructor(service: AdditionalMaster.Service2) : this(
            id = service.fragments().fullService().id().toLong(),
            type = ServiceType(service.fragments().fullService().type()),
            description = service.fragments().fullService().description(),
            price = service.fragments().fullService().price() as BigDecimal,
            duration = service.fragments().fullService().duration(),
            materials = ArrayList<Material>().apply { addAll(service.fragments().fullService().materials().map { Material(it) }) },
            photos = ArrayList<Photo>().apply { addAll(service.fragments().fullService().photos().map { Photo(it) }) },
            ctime = dateFormatter.parse(service.fragments().fullService().ctime())
    )
}