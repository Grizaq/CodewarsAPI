package com.example.tuicodewars.data.remote

import com.example.tuicodewars.data.model.DataJhoffner
import com.example.tuicodewars.data.utils.Utils.END_POINT
import retrofit2.Response
import retrofit2.http.GET

interface API {
    @GET(END_POINT)
    suspend fun getItemList(): Response<DataJhoffner>
}