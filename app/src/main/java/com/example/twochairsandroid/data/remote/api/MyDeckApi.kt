package com.example.twochairsandroid.data.remote.api

import com.example.twochairsandroid.data.remote.dto.deck.DeckDto
import com.example.twochairsandroid.data.remote.dto.game.QuestionDto
import com.example.twochairsandroid.data.remote.dto.mydeck.AddQuestionResponseDto
import com.example.twochairsandroid.data.remote.dto.mydeck.CreateDeckRequestDto
import com.example.twochairsandroid.data.remote.dto.mydeck.CreateQuestionRequestDto
import com.example.twochairsandroid.data.remote.dto.mydeck.MyDeckPickDto
import com.example.twochairsandroid.data.remote.dto.mydeck.UpdateDeckRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MyDeckApi {
    @GET("decks/my")
    suspend fun getMyDecks(): Response<List<DeckDto>>

    @GET("decks/my/can-create")
    suspend fun canCreate(): Response<Unit>

    @GET("decks/my/picker")
    suspend fun getPicker(): Response<List<MyDeckPickDto>>

    @POST("decks/my")
    suspend fun createDeck(@Body request: CreateDeckRequestDto): Response<DeckDto>

    @PATCH("decks/my/{deckId}")
    suspend fun updateDeck(
        @Path("deckId") deckId: Long,
        @Body request: UpdateDeckRequestDto,
    ): Response<DeckDto>

    @POST("decks/my/{deckId}/publish")
    suspend fun publishDeck(@Path("deckId") deckId: Long): Response<Unit>

    @POST("decks/my/{deckId}/questions")
    suspend fun addQuestion(
        @Path("deckId") deckId: Long,
        @Body request: CreateQuestionRequestDto,
    ): Response<AddQuestionResponseDto>

    @POST("decks/my/{deckId}/questions/{questionId}")
    suspend fun addExistingQuestion(
        @Path("deckId") deckId: Long,
        @Path("questionId") questionId: Long,
    ): Response<Unit>

    @GET("decks/my/{deckId}/questions")
    suspend fun getDeckQuestions(@Path("deckId") deckId: Long): Response<List<QuestionDto>>

    @DELETE("decks/my/{deckId}/questions/{questionId}")
    suspend fun removeQuestion(
        @Path("deckId") deckId: Long,
        @Path("questionId") questionId: Long,
    ): Response<Unit>
}
