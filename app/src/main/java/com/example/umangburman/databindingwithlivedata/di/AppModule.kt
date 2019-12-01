package com.example.umangburman.databindingwithlivedata.di

import android.app.Application
import com.example.umangburman.databindingwithlivedata.BuildConfig
import com.example.umangburman.databindingwithlivedata.api.ApiService
import com.example.umangburman.databindingwithlivedata.api.LiveDataCallAdapterFactory
import com.example.umangburman.databindingwithlivedata.repository.SessionManager
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {
    private val DEFAULT_TIME_OUT = 120L
    private val apiURL = "https://api.dev.karta.com"

    inner class SessionIdInterceptor(private val sessionManager: SessionManager) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            val sessionId = sessionManager.sessionId
            if (!sessionId.isNullOrEmpty()) {
                builder.addHeader("session_id", sessionId)
            }
            return chain.proceed(builder.build())
        }
    }

    @Provides
    @Singleton
    internal fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    @Provides
    @Singleton
    @Named("ApiService")
    fun provideRetrofit(interceptor: HttpLoggingInterceptor, sessionManager: SessionManager): Retrofit {
        val client = OkHttpClient().newBuilder()
                .addInterceptor(SessionIdInterceptor(sessionManager))
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .build()

        return Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideSessionManager(application: Application): SessionManager {
        return SessionManager(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("ApiService") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}