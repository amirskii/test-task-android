package com.example.amirskii.wsatlanttest.repository

import android.arch.lifecycle.LiveData
import android.util.Log
import com.example.amirskii.wsatlanttest.Model.LoginResponse
import com.example.amirskii.wsatlanttest.Model.Resource
import com.example.amirskii.wsatlanttest.api.ApiResponse
import com.example.amirskii.wsatlanttest.api.ApiService
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyRepository @Inject constructor(val service: ApiService, val sessionManager: SessionManager) {


    init {
    }

    fun <T>getNetworkData(serviceCall: () -> LiveData<ApiResponse<T>>): LiveData<Resource<T>> {
        return object: NetworkOnlyRepository<T, T>() {
            override fun saveLoadedData(item: T) {
            }

            override fun fetchService(): LiveData<ApiResponse<T>> {
                return serviceCall()
            }

            override fun onFetchFailed(error: Throwable?) {
                Log.d("111", "onFetchFailed : $error")
            }

        }.asLiveData()
    }

    fun json2Body(json: String) = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

    fun login(email: String, password: String): LiveData<Resource<LoginResponse>> {
        val body = json2Body("{ \"email\": \"$email\", \"password\": \"$password\" }")
        return getNetworkData{ service.login(body) }
    }

    fun saveToken(token: String, userInfo: String) = sessionManager.saveToken(token, userInfo)
    fun getSession() = sessionManager.sessionId
    fun getUserInfo() = sessionManager.userInfo

    fun deleteToken() = sessionManager.deleteToken()
}
