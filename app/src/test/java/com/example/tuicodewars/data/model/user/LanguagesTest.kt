package com.example.tuicodewars.data.model.user

import junit.framework.TestCase.assertEquals
import org.junit.Test

class LanguagesTest {

    private val mockLanguages = Languages(
        coffeescript = Coffeescript(rank = -4, name = "4 kyu", color = "blue", score = 903),
        java = Java(rank = -6, name = "6 kyu", color = "yellow", score = 100),
        haskell = Haskell(rank = -6, name = "6 kyu", color = "yellow", score = 126),
        typescript = Typescript(rank = -7, name = "7 kyu", color = "white", score = 22),
        csharp = Csharp(rank = -6, name = "6 kyu", color = "yellow", score = 90),
        crystal = Crystal(rank = -8, name = "8 kyu", color = "white", score = 4),
        javascript = Javascript(rank = -3, name = "3 kyu", color = "blue", score = 2683),
        ruby = Ruby(rank = -3, name = "3 kyu", color = "blue", score = 1906),
        python = Python(rank = -5, name = "5 kyu", color = "yellow", score = 357),
        elixir = Elixir(rank = -8, name = "8 kyu", color = "white", score = 2),
        sql = Sql(rank = -6, name = "6 kyu", color = "yellow", score = 86),
        shell = Shell(rank = -8, name = "8 kyu", color = "white", score = 5),
        objc = Objc(rank = -8, name = "8 kyu", color = "white", score = 4)
    )
    private val expectedLanguages = listOf(
        "coffeescript",
        "crystal",
        "csharp",
        "elixir",
        "haskell",
        "java",
        "javascript",
        "objc",
        "python",
        "ruby",
        "shell",
        "sql",
        "typescript"
    )

    @Test
    fun testGetLanguageNames() {
        assertEquals(expectedLanguages, Languages.getLanguageNames(mockLanguages))
    }
}