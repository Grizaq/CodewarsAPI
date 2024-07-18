package com.example.tuicodewars.data.model.authored

data class Data(
    val description: String,
    val id: String,
    val languages: List<String>,
    val name: String,
    val rank: Int,
    val rankName: String,
    val tags: List<String>
)