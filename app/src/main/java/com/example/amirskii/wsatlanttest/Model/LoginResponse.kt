package com.example.amirskii.wsatlanttest.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class LoginResponse {
    @SerializedName("token")
    @Expose
    var token: String? = null
}
