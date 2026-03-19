package com.example.twochairsandroid.data.remote.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    val errorCode: String,
    val message: String,
    val path: String,
    val timestamp: String,
)
