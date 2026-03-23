package com.example.twochairsandroid.core.network

import com.example.twochairsandroid.core.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(
    private val tokenStorage: TokenStorage,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenStorage.getAccessToken() }
        val path = chain.request().url.encodedPath
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank() && !path.contains("/auth/")) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(request)
    }
}
