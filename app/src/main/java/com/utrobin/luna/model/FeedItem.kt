package com.utrobin.luna.model

/**
 * Created by ivan on 29.10.2017.
 */

data class FeedItem(val name: String,
                    val avatar: String,
                    val address: String,
                    val achievements: List<Achievement> = ArrayList(),
                    val photos: List<String> = ArrayList(),
                    val stars: Double)


enum class Achievement {
    CAREFUL, FRIENDLY, FAST
}