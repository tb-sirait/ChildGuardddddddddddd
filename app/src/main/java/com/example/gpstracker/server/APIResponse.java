package com.example.gpstracker.server;

import okhttp3.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface APIResponse {
    @GET("/parents")
    suspend fun get_orangtua(): Response<get_ortu>

    @PUT("/parents/update")
    suspend fun updateOrtu(@Body requestData: YourRequestType): Response<YourResponseType>
}
