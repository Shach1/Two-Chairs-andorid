package com.example.twochairsandroid.domain.model

data class Product(
    val id: Long,
    val type: ProductType,
    val title: String,
    val priceRub: Int,
    val deckId: Long?,
    val active: Boolean,
)

enum class ProductType {
    DECK,
    FEATURE_CREATE_DECKS,
    PREMIUM,
    UNKNOWN;

    companion object {
        fun fromRaw(raw: String): ProductType = entries.firstOrNull { it.name == raw } ?: UNKNOWN
    }
}

data class PurchaseResult(
    val purchaseId: Long,
    val productId: Long,
    val productType: ProductType,
)
