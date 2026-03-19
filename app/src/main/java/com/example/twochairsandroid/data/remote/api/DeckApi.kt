package com.example.twochairsandroid.data.remote.api

import com.example.twochairsandroid.data.remote.dto.deck.DeckDto
import retrofit2.Response
import retrofit2.http.GET

interface DeckApi {
    @GET("decks")
    suspend fun getAccessibleDecks(): Response<List<DeckDto>>

    @GET("decks/store")
    suspend fun getStoreDecks(): Response<List<DeckDto>>
}
