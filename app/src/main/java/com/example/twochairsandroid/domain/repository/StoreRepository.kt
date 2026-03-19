package com.example.twochairsandroid.domain.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.Product
import com.example.twochairsandroid.domain.model.PurchaseResult

interface StoreRepository {
    suspend fun getProducts(): ApiResult<List<Product>>
    suspend fun purchase(productId: Long): ApiResult<PurchaseResult>
}
