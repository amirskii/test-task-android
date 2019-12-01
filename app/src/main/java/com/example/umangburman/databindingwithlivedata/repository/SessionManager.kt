package com.example.umangburman.databindingwithlivedata.repository


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.auth0.android.jwt.JWT

class SessionManager(private val context: Context) {
    private val mPreferences: SharedPreferences
    var sessionId: String = ""
    var userInfo: String = ""

    init {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)

        if (mPreferences.contains("token")) {
            val token = mPreferences.getString("token", "")
            loadSession(token)
        }
        if (mPreferences.contains("userInfo")) {
            userInfo = mPreferences.getString("userInfo", "")
        }
    }

    fun loadSession(token: String) {
        val jwt = JWT(token)
        jwt.getClaim("session_id").asString()?.let {
            this.sessionId = it
            Log.d(TAG, "load session $sessionId expires at ${jwt.expiresAt}")
        }
    }

    fun saveToken(token: String, userInfo: String) {
        loadSession(token)
        mPreferences.edit().putString("token", token).apply()
        mPreferences.edit().putString("userInfo", userInfo).apply()
    }

    companion object {
        val TAG = SessionManager::class.java.simpleName
    }

}
