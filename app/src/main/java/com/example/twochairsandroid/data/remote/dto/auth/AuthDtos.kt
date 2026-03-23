package com.example.twochairsandroid.data.remote.dto.auth

import com.example.twochairsandroid.domain.model.AuthSession
import com.example.twochairsandroid.domain.model.SmsCodeInfo
import com.example.twochairsandroid.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendCodeRequestDto(
    val phoneNumber: String,
)

@Serializable
data class SendCodeResponseDto(
    val expiresInSeconds: Int,
)

@Serializable
data class VerifyCodeRequestDto(
    val phoneNumber: String,
    val code: String,
)

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val user: UserDto,
)

@Serializable
data class UserDto(
    val id: Long,
    val phoneNumber: String,
    @SerialName("premium") val premium: Boolean? = null,
    @SerialName("isPremium") val isPremium: Boolean? = null,
)

fun SendCodeResponseDto.toDomain(): SmsCodeInfo = SmsCodeInfo(expiresInSeconds = expiresInSeconds)

fun AuthResponseDto.toDomain(): AuthSession = AuthSession(
    accessToken = accessToken,
    user = user.toDomain(),
)

fun UserDto.toDomain(): User = User(
    id = id,
    phoneNumber = phoneNumber,
    isPremium = premium ?: isPremium ?: false,
)
