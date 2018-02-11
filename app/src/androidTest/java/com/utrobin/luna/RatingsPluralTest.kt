package com.utrobin.luna

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RatingsPluralTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        if (Locale.getDefault().language != "ru") {
            return      // We can't test it on emulator with English locale
        }
        for (i in 0..500) {
            val testString = appContext.resources.getQuantityString(R.plurals.ratings_count, i, "", i)
            val actual = testString.split("\\s+".toRegex()).last().toLowerCase()
            val expected = getWordByQuantity(i).toLowerCase()
            assertEquals("Testing with quantity = $i,", expected, actual)
        }
    }

    private fun getWordByQuantity(quantity: Int): String {
        if (quantity % 100 in 11..14) {
            return MANY
        }
        return when (quantity % 10) {
            0 -> MANY
            1 -> ONE
            2, 3, 4 -> FEW
            else -> MANY
        }
    }


    companion object {
        private const val ONE = "оценка"
        private const val FEW = "оценки"
        private const val MANY = "оценок"
    }
}
