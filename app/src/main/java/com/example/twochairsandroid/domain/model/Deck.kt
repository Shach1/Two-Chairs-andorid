package com.example.twochairsandroid.domain.model

data class Deck(
    val id: Long,
    val type: DeckType,
    val visibility: DeckVisibility,
    val title: String,
    val description: String?,
    val ageRating: Int,
    val priceRub: Int,
    val locked: Boolean,
    val ownerUserId: Long?,
)

enum class DeckType {
    DEFAULT,
    PAID,
    USER,
    UNKNOWN;

    companion object {
        fun fromRaw(raw: String): DeckType = entries.firstOrNull { it.name == raw } ?: UNKNOWN
    }
}

enum class DeckVisibility {
    PRIVATE,
    PUBLIC,
    UNKNOWN;

    companion object {
        fun fromRaw(raw: String): DeckVisibility = entries.firstOrNull { it.name == raw } ?: UNKNOWN
    }
}

data class MyDeckPick(
    val id: Long,
    val title: String,
)
