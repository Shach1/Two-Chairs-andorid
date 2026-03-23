package com.example.twochairsandroid.domain.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.Deck
import com.example.twochairsandroid.domain.model.MyDeckPick
import com.example.twochairsandroid.domain.model.Question

interface MyDeckRepository {
    suspend fun getMyDecks(): ApiResult<List<Deck>>
    suspend fun canCreateDecks(): ApiResult<Boolean>
    suspend fun getDeckPicker(): ApiResult<List<MyDeckPick>>
    suspend fun createDeck(title: String, description: String?, ageRating: Int): ApiResult<Deck>
    suspend fun updateDeck(deckId: Long, title: String?, description: String?, ageRating: Int?): ApiResult<Deck>
    suspend fun publishDeck(deckId: Long): ApiResult<Unit>
    suspend fun addQuestion(deckId: Long, optionA: String, optionB: String): ApiResult<Long>
    suspend fun addExistingQuestion(deckId: Long, questionId: Long): ApiResult<Unit>
    suspend fun getDeckQuestions(deckId: Long): ApiResult<List<Question>>
    suspend fun removeQuestion(deckId: Long, questionId: Long): ApiResult<Unit>
}
