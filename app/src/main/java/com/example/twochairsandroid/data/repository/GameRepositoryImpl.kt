package com.example.twochairsandroid.data.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.core.network.requireBody
import com.example.twochairsandroid.core.network.safeApiCall
import com.example.twochairsandroid.data.remote.api.GameApi
import com.example.twochairsandroid.data.remote.dto.game.AnswerRequestDto
import com.example.twochairsandroid.data.remote.dto.game.toDomain
import com.example.twochairsandroid.domain.model.AnswerOption
import com.example.twochairsandroid.domain.model.AnswerStats
import com.example.twochairsandroid.domain.model.Question
import com.example.twochairsandroid.domain.repository.GameRepository
import kotlinx.serialization.json.Json

class GameRepositoryImpl(
    private val gameApi: GameApi,
    private val json: Json,
) : GameRepository {

    override suspend fun getNextQuestion(deckId: Long): ApiResult<Question?> {
        return safeApiCall(
            json = json,
            execute = { gameApi.getNextQuestion(deckId = deckId) },
            mapSuccess = { body -> body?.toDomain() },
        )
    }

    override suspend fun submitAnswer(
        deckId: Long,
        questionId: Long,
        answer: AnswerOption,
    ): ApiResult<AnswerStats> {
        return safeApiCall(
            json = json,
            execute = {
                gameApi.submitAnswer(
                    deckId = deckId,
                    questionId = questionId,
                    request = AnswerRequestDto(answer = answer.rawValue),
                )
            },
            mapSuccess = { body -> body.requireBody().toDomain() },
        )
    }
}
