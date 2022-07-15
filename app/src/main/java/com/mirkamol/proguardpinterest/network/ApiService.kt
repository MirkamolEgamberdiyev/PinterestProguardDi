package com.mirkamol.proguardpinterest.network

import com.mirkamol.proguardpinterest.model.HomePhotoItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<HomePhotoItem>

}