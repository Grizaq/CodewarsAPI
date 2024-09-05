package com.example.tuicodewars.data.utils.converters

import com.example.tuicodewars.data.model.authored.Data
import com.example.tuicodewars.data.model.user.LanguageDetails
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {

    private val converters = Converters()
    private val gson = Gson()

    @Test
    fun `test fromStringList converts List to JSON string`() {
        val list = listOf("Kotlin", "Java", "Python")
        val expectedJson = gson.toJson(list)

        val actualJson = converters.fromStringList(list)
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun `test toStringList converts JSON string to List`() {
        val json = """["Kotlin","Java","Python"]"""
        val expectedList = listOf("Kotlin", "Java", "Python")

        val actualList = converters.toStringList(json)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `test fromDataList converts List of Data to JSON string`() {
        val dataList = listOf(
            Data("Description1", "1", listOf("Kotlin"), "Data1", 1, "4 Kyu", listOf("Tag1")),
            Data("Description2", "2", listOf("Java"), "Data2", 2, "6 kyu", listOf("Tag2"))
        )
        val expectedJson = gson.toJson(dataList)

        val actualJson = converters.fromDataList(dataList)
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun `test toDataList converts JSON string to List of Data`() {
        val json =
            """[{"id":"1","description":"Description1","languages":["Kotlin"],"name":"Data1","rank":1,"rankName":"4 Kyu","tags":["Tag1"]},{"id":"2","description":"Description2","languages":["Java"],"name":"Data2","rank":2,"rankName":"6 kyu","tags":["Tag2"]}]"""
        val expectedList = listOf(
            Data("1", "Description1", listOf("Kotlin"), "Data1", 1, "4 Kyu", listOf("Tag1")),
            Data("2", "Description2", listOf("Java"), "Data2", 2, "6 kyu", listOf("Tag2"))
        )

        val actualList = converters.toDataList(json)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `test fromLanguagesMap converts Map to JSON string`() {
        val languagesMap = mapOf(
            "Kotlin" to LanguageDetails("Blue", "Kotlin", 1, 100),
            "Java" to LanguageDetails("Red", "Java", 2, 95)
        )
        val expectedJson = gson.toJson(languagesMap)

        val actualJson = converters.fromLanguagesMap(languagesMap)
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun `test toLanguagesMap converts JSON string to Map`() {
        val json =
            """{"Kotlin":{"color":"Blue","name":"Kotlin","rank":1,"score":100},"Java":{"color":"Red","name":"Java","rank":2,"score":95}}"""
        val expectedMap = mapOf(
            "Kotlin" to LanguageDetails("Blue", "Kotlin", 1, 100),
            "Java" to LanguageDetails("Red", "Java", 2, 95)
        )

        val actualMap = converters.toLanguagesMap(json)
        assertEquals(expectedMap, actualMap)
    }
}