package com.example.twochairsandroid.data.remote.dto.mydeck

import com.example.twochairsandroid.domain.model.MyDeckPick
import kotlinx.serialization.Serializable

@Serializable
data class MyDeckPickDto(
    val id: Long,
    val title: String,
)

@Serializable
data class CreateDeckRequestDto(
    val title: String,
    val description: String?,
    val ageRating: Int,
)

@Serializable
data class UpdateDeckRequestDto(
    val title: String? = null,
    val description: String? = null,
    val ageRating: Int? = null,
)

@Serializable
data class CreateQuestionRequestDto(
    val optionA: String,
    val optionB: String,
)

@Serializable
data class AddQuestionResponseDto(
    val questionId: Long,
)

fun MyDeckPickDto.toDomain(): MyDeckPick = MyDeckPick(
    id = id,
    title = title,
)
