package com.example.tuicodewars.data.utils.converters

import androidx.room.TypeConverter
import com.example.tuicodewars.data.model.authored.Data
import com.example.tuicodewars.data.model.user.LanguageDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    // Converters for List<String>
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    // Converters for List<Data>
    @TypeConverter
    fun fromDataList(value: List<Data>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDataList(value: String?): List<Data>? {
        val listType = object : TypeToken<List<Data>>() {}.type
        val dataList = gson.fromJson<List<Data>>(value, listType)
        return dataList?.map { it.copy(logoUrls = it.logoUrls ?: emptyList()) }
    }

    // Converters for Map<String, LanguageDetails>
    @TypeConverter
    fun fromLanguagesMap(value: Map<String, LanguageDetails>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toLanguagesMap(value: String?): Map<String, LanguageDetails>? {
        val mapType = object : TypeToken<Map<String, LanguageDetails>>() {}.type
        return value?.let { gson.fromJson(it, mapType) }
    }
}