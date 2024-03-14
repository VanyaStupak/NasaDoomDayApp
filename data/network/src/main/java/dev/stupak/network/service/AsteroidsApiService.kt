package dev.stupak.network.service

import dev.stupak.network.model.NearEarthObject
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidsApiService {
    @GET("feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ): NearEarthObject
}
