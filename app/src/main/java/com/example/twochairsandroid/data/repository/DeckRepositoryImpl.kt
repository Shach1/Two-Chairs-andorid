package com.example.twochairsandroid.data.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.core.network.safeApiCall
import com.example.twochairsandroid.data.remote.api.DeckApi
import com.example.twochairsandroid.data.remote.dto.deck.toDomain
import com.example.twochairsandroid.domain.model.Deck
import com.example.twochairsandroid.domain.repository.DeckRepository
import kotlinx.serialization.json.Json

class DeckRepositoryImpl(
    private val deckApi: DeckApi,
    private val json: Json,
) : DeckRepository {

    override suspend fun getAccessibleDecks(): ApiResult<List<Deck>> {
        return safeApiCall(
            json = json,
            execute = { deckApi.getAccessibleDecks() },
            mapSuccess = { body -> body.orEmpty().map { it.toDomain() } },
        )
    }

    override suspend fun getStoreDecks(): ApiResult<List<Deck>> {
        return safeApiCall(
            json = json,
            execute = { deckApi.getStoreDecks() },
            mapSuccess = { body -> body.orEmpty().map { it.toDomain() } },
        )
    }
}
