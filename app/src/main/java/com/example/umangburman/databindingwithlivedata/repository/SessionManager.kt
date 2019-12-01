package com.example.umangburman.databindingwithlivedata.repository


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.auth0.android.jwt.JWT

class SessionManager(private val context: Context) {
    private val mPreferences: SharedPreferences
    var sessionId: String = ""

    init {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        //sessionId = mPreferences.getString("sessionId", "")

        if (mPreferences.contains("token")) {
            val token = mPreferences.getString("token", "")
            loadSession(token)
        }
    }

    fun loadSession(token: String) {
        val jwt = JWT(token)
        jwt.getClaim("session_id").asString()?.let {
            this.sessionId = it
            Log.d(TAG, "load session $sessionId expires at ${jwt.expiresAt}")
        }
    }
//
//    fun saveSession(sessionId: String) {
//        this.sessionId = sessionId
//        mPreferences.edit().putString("sessionId", sessionId).apply()
//    }

    fun saveToken(token: String) {
        loadSession(token)
        mPreferences.edit().putString("token", token).apply()
    }

    companion object {
        val TAG = SessionManager::class.java.simpleName
    }

}
