package com.utrobin.luna.model

/**
 * Created by ivan on 29.10.2017.
 */

data class FeedItem(val name: String,
                    val avatar: Photo,
                    val address: Address,
                    val stars: Double,
                    val signs: List<Sign> = ArrayList(),
                    val photos: List<Photo> = ArrayList()
)