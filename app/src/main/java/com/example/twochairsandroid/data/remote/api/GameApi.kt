package com.example.twochairsandroid.data.remote.api

import com.example.twochairsandroid.data.remote.dto.game.AnswerRequestDto
import com.example.twochairsandroid.data.remote.dto.game.AnswerStatsDto
import com.example.twochairsandroid.data.remote.dto.game.QuestionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GameApi {
    @GET("game/decks/{deckId}/next-question")
    suspend fun getNextQuestion(@Path("deckId") deckId: Long): Response<QuestionDto>

    @POST("game/decks/{deckId}/questions/{questionId}/answer")
    suspend fun submitAnswer(
        @Path("deckId") deckId: Long,
        @Path("questionId") questionId: Long,
        @Body request: AnswerRequestDto,
    ): Response<AnswerStatsDto>
}
