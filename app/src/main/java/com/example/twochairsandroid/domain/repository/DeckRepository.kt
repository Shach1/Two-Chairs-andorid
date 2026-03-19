package com.example.twochairsandroid.domain.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.Deck

interface DeckRepository {
    suspend fun getAccessibleDecks(): ApiResult<List<Deck>>
    suspend fun getStoreDecks(): ApiResult<List<Deck>>
}
