package com.example.amirskii.wsatlanttest.api

import android.arch.lifecycle.LiveData
import com.example.amirskii.wsatlanttest.Model.LoginResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("accounts/auth")
    fun login(@Body params: RequestBody): LiveData<ApiResponse<LoginResponse>>
}