package com.example.tuicodewars.data.model.challenge

import com.example.tuicodewars.data.model.challenge.Challenge.Companion.shortenLongWords
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ChallengeTest {
    private val initialText =
        "some sentence which has one veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrryyyyyyyyyyy long words"
    private val resultText = "some sentence which has one veeee[...]yyyyy long words"
    private val textWithNoLongWords = "some sentence with no long words"

    @Test
    fun testShortenLongWordsReturnsShortenedWords() {
        assertEquals(resultText, initialText.shortenLongWords())
    }

    @Test
    fun testShortenLongWordsNotAffectingOtherStrings() {
        assertEquals(textWithNoLongWords, textWithNoLongWords.shortenLongWords())
    }
}