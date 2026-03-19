package com.example.twochairsandroid.data.remote.api

import com.example.twochairsandroid.data.remote.dto.auth.AuthResponseDto
import com.example.twochairsandroid.data.remote.dto.auth.SendCodeRequestDto
import com.example.twochairsandroid.data.remote.dto.auth.SendCodeResponseDto
import com.example.twochairsandroid.data.remote.dto.auth.VerifyCodeRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/send-code")
    suspend fun sendCode(@Body request: SendCodeRequestDto): Response<SendCodeResponseDto>

    @POST("auth/verify-code")
    suspend fun verifyCode(@Body request: VerifyCodeRequestDto): Response<AuthResponseDto>
}
