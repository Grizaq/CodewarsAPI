package com.example.tuicodewars.data.model.user

data class DataJhoffner(
    val clan: String,
    val codeChallenges: CodeChallenges,
    val honor: Int,
    val id: String,
    val leaderboardPosition: Int,
    val name: String,
    val ranks: Ranks,
    val skills: List<String>,
    val username: String
)