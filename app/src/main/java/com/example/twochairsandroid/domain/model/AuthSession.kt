package com.example.twochairsandroid.domain.model

data class AuthSession(
    val accessToken: String,
    val user: User,
)

data class User(
    val id: Long,
    val phoneNumber: String,
    val isPremium: Boolean,
)

data class SmsCodeInfo(
    val expiresInSeconds: Int,
)
