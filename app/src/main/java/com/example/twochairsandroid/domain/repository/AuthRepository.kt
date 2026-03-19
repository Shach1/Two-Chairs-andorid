package com.example.twochairsandroid.domain.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.AuthSession
import com.example.twochairsandroid.domain.model.SmsCodeInfo
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val accessTokenFlow: Flow<String?>

    suspend fun getAccessToken(): String?
    suspend fun sendCode(phoneNumber: String): ApiResult<SmsCodeInfo>
    suspend fun verifyCode(phoneNumber: String, code: String): ApiResult<AuthSession>
    suspend fun clearSession()
}
