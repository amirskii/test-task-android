package com.example.umangburman.databindingwithlivedata.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.example.umangburman.databindingwithlivedata.Model.Resource
import com.example.umangburman.databindingwithlivedata.api.ApiResponse

/**
 * Created by skydoves on 2018. 3. 6.
 * Copyright (c) 2018 skydoves All rights reserved.
 */

abstract class NetworkOnlyRepository<ResultType, RequestType>
internal constructor() {

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        result.postValue(Resource.loading(null, null))
        val apiResponse = fetchService()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when(response?.isSuccessful) {
                true -> {
                    saveLoadedData(response.body as ResultType)
                    setValue(Resource.success(response.body as ResultType?, response.serverDate))
                }
                false -> {
                    onFetchFailed(response.error)
                    setValue(Resource.error(response.error.toString(), null, response.error))
                }
            }
        }

    }

    @WorkerThread
    protected abstract fun saveLoadedData(item: ResultType)

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        result.value = newValue
    }

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @MainThread
    protected abstract fun fetchService(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected abstract fun onFetchFailed(error: Throwable?)
}
