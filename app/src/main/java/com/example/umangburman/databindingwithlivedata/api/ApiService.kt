package com.example.umangburman.databindingwithlivedata.api

import android.arch.lifecycle.LiveData
import com.example.umangburman.databindingwithlivedata.Model.BaseResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("accounts/auth")
    fun login(@Body params: RequestBody): LiveData<ApiResponse<BaseResponse>>
}