package com.example.borrowbox.api

import com.example.borrowbox.model.LoginRequest
import com.example.borrowbox.model.LoginResponse
import com.example.borrowbox.model.MeResponse
import com.example.borrowbox.model.RegisterRequest
import com.example.borrowbox.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/user/me")
    fun getMe(@Header("Authorization") token: String): Call<MeResponse>
}