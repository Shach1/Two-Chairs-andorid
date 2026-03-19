package com.example.twochairsandroid.data.remote.dto.game

import com.example.twochairsandroid.domain.model.AnswerStats
import com.example.twochairsandroid.domain.model.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val id: Long,
    val deckId: Long,
    val optionA: String,
    val optionB: String,
)

@Serializable
data class AnswerRequestDto(
    val answer: String,
)

@Serializable
data class AnswerStatsDto(
    val pctA: Int,
    val pctB: Int,
)

fun QuestionDto.toDomain(): Question = Question(
    id = id,
    deckId = deckId,
    optionA = optionA,
    optionB = optionB,
)

fun AnswerStatsDto.toDomain(): AnswerStats = AnswerStats(
    pctA = pctA,
    pctB = pctB,
)
