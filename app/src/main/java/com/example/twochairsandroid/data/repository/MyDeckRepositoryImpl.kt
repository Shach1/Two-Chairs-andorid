package com.example.twochairsandroid.data.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.core.network.requireBody
import com.example.twochairsandroid.core.network.safeApiCall
import com.example.twochairsandroid.data.remote.api.MyDeckApi
import com.example.twochairsandroid.data.remote.dto.deck.toDomain
import com.example.twochairsandroid.data.remote.dto.game.toDomain
import com.example.twochairsandroid.data.remote.dto.mydeck.CreateDeckRequestDto
import com.example.twochairsandroid.data.remote.dto.mydeck.CreateQuestionRequestDto
import com.example.twochairsandroid.data.remote.dto.mydeck.UpdateDeckRequestDto
import com.example.twochairsandroid.data.remote.dto.mydeck.toDomain
import com.example.twochairsandroid.domain.model.Deck
import com.example.twochairsandroid.domain.model.MyDeckPick
import com.example.twochairsandroid.domain.model.Question
import com.example.twochairsandroid.domain.repository.MyDeckRepository
import kotlinx.serialization.json.Json

class MyDeckRepositoryImpl(
    private val myDeckApi: MyDeckApi,
    private val json: Json,
) : MyDeckRepository {

    override suspend fun getMyDecks(): ApiResult<List<Deck>> {
        return safeApiCall(
            json = json,
            execute = { myDeckApi.getMyDecks() },
            mapSuccess = { body -> body.orEmpty().map { it.toDomain() } },
        )
    }

    override suspend fun getDeckPicker(): ApiResult<List<MyDeckPick>> {
        return safeApiCall(
            json = json,
            execute = { myDeckApi.getPicker() },
            mapSuccess = { body -> body.orEmpty().map { it.toDomain() } },
        )
    }

    override suspend fun createDeck(
        title: String,
        description: String?,
        ageRating: Int,
    ): ApiResult<Deck> {
        return safeApiCall(
            json = json,
            execute = {
                myDeckApi.createDeck(
                    request = CreateDeckRequestDto(
                        title = title,
                        description = description,
                        ageRating = ageRating,
                    )
                )
            },
            mapSuccess = { body -> body.requireBody().toDomain() },
        )
    }

    override suspend fun updateDeck(
        deckId: Long,
        title: String?,
        description: String?,
        ageRating: Int?,
    ): ApiResult<Deck> {
        return safeApiCall(
            json = json,
            execute = {
                myDeckApi.updateDeck(
                    deckId = deckId,
                    request = UpdateDeckRequestDto(
                        title = title,
                        description = description,
                        ageRating = ageRating,
                    ),
                )
            },
            mapSuccess = { body -> body.requireBody().toDomain() },
        )
    }

    override suspend fun publishDeck(deckId: Long): ApiResult<Unit> {
        return safeApiCall(
            json = json,
            execute = { myDeckApi.publishDeck(deckId = deckId) },
            mapSuccess = { Unit },
        )
    }

    override suspend fun addQuestion(
        deckId: Long,
        optionA: String,
        optionB: String,
    ): ApiResult<Long> {
        return safeApiCall(
            json = json,
            execute = {
                myDeckApi.addQuestion(
                    deckId = deckId,
                    request = CreateQuestionRequestDto(
                        optionA = optionA,
                        optionB = optionB,
                    ),
                )
            },
            mapSuccess = { body -> body.requireBody().questionId },
        )
    }

    override suspend fun addExistingQuestion(deckId: Long, questionId: Long): ApiResult<Unit> {
        return safeApiCall(
            json = json,
            execute = {
                myDeckApi.addExistingQuestion(
                    deckId = deckId,
                    questionId = questionId,
                )
            },
            mapSuccess = { Unit },
        )
    }

    override suspend fun getDeckQuestions(deckId: Long): ApiResult<List<Question>> {
        return safeApiCall(
            json = json,
            execute = { myDeckApi.getDeckQuestions(deckId = deckId) },
            mapSuccess = { body -> body.orEmpty().map { it.toDomain() } },
        )
    }

    override suspend fun removeQuestion(deckId: Long, questionId: Long): ApiResult<Unit> {
        return safeApiCall(
            json = json,
            execute = {
                myDeckApi.removeQuestion(
                    deckId = deckId,
                    questionId = questionId,
                )
            },
            mapSuccess = { Unit },
        )
    }
}
