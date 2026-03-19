package com.example.twochairsandroid.data.remote.dto.deck

import com.example.twochairsandroid.domain.model.Deck
import com.example.twochairsandroid.domain.model.DeckType
import com.example.twochairsandroid.domain.model.DeckVisibility
import kotlinx.serialization.Serializable

@Serializable
data class DeckDto(
    val id: Long,
    val type: String,
    val visibility: String,
    val title: String,
    val description: String? = null,
    val ageRating: Int,
    val priceRub: Int,
    val locked: Boolean,
    val ownerUserId: Long? = null,
)

fun DeckDto.toDomain(): Deck = Deck(
    id = id,
    type = DeckType.fromRaw(type),
    visibility = DeckVisibility.fromRaw(visibility),
    title = title,
    description = description,
    ageRating = ageRating,
    priceRub = priceRub,
    locked = locked,
    ownerUserId = ownerUserId,
)
