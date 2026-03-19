package com.example.twochairsandroid.data.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.core.network.requireBody
import com.example.twochairsandroid.core.network.safeApiCall
import com.example.twochairsandroid.core.storage.TokenStorage
import com.example.twochairsandroid.data.remote.api.AuthApi
import com.example.twochairsandroid.data.remote.dto.auth.SendCodeRequestDto
import com.example.twochairsandroid.data.remote.dto.auth.VerifyCodeRequestDto
import com.example.twochairsandroid.data.remote.dto.auth.toDomain
import com.example.twochairsandroid.domain.model.AuthSession
import com.example.twochairsandroid.domain.model.SmsCodeInfo
import com.example.twochairsandroid.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
    private val json: Json,
) : AuthRepository {

    override val accessTokenFlow: Flow<String?> = tokenStorage.accessTokenFlow

    override suspend fun getAccessToken(): String? = tokenStorage.getAccessToken()

    override suspend fun sendCode(phoneNumber: String): ApiResult<SmsCodeInfo> {
        return safeApiCall(
            json = json,
            execute = { authApi.sendCode(SendCodeRequestDto(phoneNumber = phoneNumber)) },
            mapSuccess = { body -> body.requireBody().toDomain() },
        )
    }

    override suspend fun verifyCode(phoneNumber: String, code: String): ApiResult<AuthSession> {
        val result = safeApiCall(
            json = json,
            execute = {
                authApi.verifyCode(
                    VerifyCodeRequestDto(
                        phoneNumber = phoneNumber,
                        code = code,
                    )
                )
            },
            mapSuccess = { body -> body.requireBody().toDomain() },
        )

        if (result is ApiResult.Success) {
            tokenStorage.saveAccessToken(result.data.accessToken)
        }

        return result
    }

    override suspend fun clearSession() {
        tokenStorage.clearAccessToken()
    }
}
