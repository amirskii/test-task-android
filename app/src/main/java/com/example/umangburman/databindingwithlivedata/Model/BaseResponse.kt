package com.example.umangburman.databindingwithlivedata.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class BaseResponse {
    @SerializedName("StatusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("Message")
    @Expose
    var message: String? = null

    @SerializedName("Description")
    @Expose
    var description: String? = null
}
