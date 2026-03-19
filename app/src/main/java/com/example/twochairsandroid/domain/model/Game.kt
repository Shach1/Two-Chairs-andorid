package com.example.twochairsandroid.domain.model

data class Question(
    val id: Long,
    val deckId: Long,
    val optionA: String,
    val optionB: String,
)

data class AnswerStats(
    val pctA: Int,
    val pctB: Int,
)

enum class AnswerOption(val rawValue: String) {
    A("A"),
    B("B"),
}
