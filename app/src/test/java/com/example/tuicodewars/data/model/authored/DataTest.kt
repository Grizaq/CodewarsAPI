package com.example.tuicodewars.data.model.authored

import com.example.tuicodewars.data.model.authored.Data.Companion.toCommaSeparatedString
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DataTest {
    private val listStrings = listOf(
        "apple",
        "banana",
        "cherry",
        "date",
        "elderberry",
        "fig",
        "grape",
        "honeydew"
    )
    private val listStringsSeparated =
        "apple, banana, cherry, date, elderberry, fig, grape, honeydew"

    @Test
    fun testToCommaSeparatedStringReturnsString() {
        assertEquals(listStringsSeparated, listStrings.toCommaSeparatedString())
    }
}
