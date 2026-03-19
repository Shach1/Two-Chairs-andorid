package com.example.twochairsandroid.domain.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.AnswerOption
import com.example.twochairsandroid.domain.model.AnswerStats
import com.example.twochairsandroid.domain.model.Question

interface GameRepository {
    suspend fun getNextQuestion(deckId: Long): ApiResult<Question?>
    suspend fun submitAnswer(deckId: Long, questionId: Long, answer: AnswerOption): ApiResult<AnswerStats>
}
