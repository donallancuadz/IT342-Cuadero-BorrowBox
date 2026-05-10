package com.example.borrowbox.shared.api

import retrofit2.http.PUT
import com.example.borrowbox.shared.model.UpdateProfileRequest
import com.example.borrowbox.shared.model.BorrowRequestCreateRequest
import com.example.borrowbox.shared.model.BorrowRequestResponse
import com.example.borrowbox.shared.model.DashboardStats
import com.example.borrowbox.shared.model.Item
import com.example.borrowbox.shared.model.LoginRequest
import com.example.borrowbox.shared.model.LoginResponse
import com.example.borrowbox.shared.model.MeResponse
import com.example.borrowbox.shared.model.RegisterRequest
import com.example.borrowbox.shared.model.RegisterResponse
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

    @GET("api/user/dashboard")
    fun getDashboard(@Header("Authorization") token: String): Call<DashboardStats>

    @GET("api/items")
    fun getAllItems(@Header("Authorization") token: String): Call<List<Item>>

    @POST("api/requests")
    fun createRequest(
        @Header("Authorization") token: String,
        @Body request: BorrowRequestCreateRequest
    ): Call<BorrowRequestResponse>

    @PUT("api/user/me")
    fun updateMe(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Call<MeResponse>

    @GET("api/requests/my")
    fun getMyRequests(@Header("Authorization") token: String): Call<List<BorrowRequestResponse>>
}