package com.example.twochairsandroid.data.remote.dto.store

import com.example.twochairsandroid.domain.model.Product
import com.example.twochairsandroid.domain.model.ProductType
import com.example.twochairsandroid.domain.model.PurchaseResult
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Long,
    val type: String,
    val title: String,
    val priceRub: Int,
    val deckId: Long? = null,
    val active: Boolean,
)

@Serializable
data class PurchaseRequestDto(
    val productId: Long,
)

@Serializable
data class PurchaseResponseDto(
    val purchaseId: Long,
    val productId: Long,
    val productType: String,
)

fun ProductDto.toDomain(): Product = Product(
    id = id,
    type = ProductType.fromRaw(type),
    title = title,
    priceRub = priceRub,
    deckId = deckId,
    active = active,
)

fun PurchaseResponseDto.toDomain(): PurchaseResult = PurchaseResult(
    purchaseId = purchaseId,
    productId = productId,
    productType = ProductType.fromRaw(productType),
)
