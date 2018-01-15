package com.utrobin.luna.model

/**
 * Created by ivan on 15.01.2018.
 */

data class FeedItem(val userId: Long,
                    val name: String,
                    val avatar: Photo,
                    val stars: Double,
                    val signs: List<Sign>,
                    val photos: List<Photo>,
                    var isFavorite: Boolean = false
)