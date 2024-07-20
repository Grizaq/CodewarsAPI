package com.example.tuicodewars.data.remote

import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.utils.Utils.END_POINT
import com.example.tuicodewars.data.utils.Utils.END_POINT_AUTHORED
import com.example.tuicodewars.data.utils.Utils.END_POINT_CHALLENGE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface API {
    @GET(END_POINT)
    suspend fun getItemList(): Response<DataJhoffner>

    @GET(END_POINT_AUTHORED)
    suspend fun getAuthoredList(): Response<Authored>

    @GET(END_POINT_CHALLENGE)
    suspend fun getChallengeData(@Path("id") id: String): Response<Challenge>
}