package com.example.twochairsandroid.data.repository

import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.core.network.requireBody
import com.example.twochairsandroid.core.network.safeApiCall
import com.example.twochairsandroid.data.remote.api.StoreApi
import com.example.twochairsandroid.data.remote.dto.store.PurchaseRequestDto
import com.example.twochairsandroid.data.remote.dto.store.toDomain
import com.example.twochairsandroid.domain.model.Product
import com.example.twochairsandroid.domain.model.PurchaseResult
import com.example.twochairsandroid.domain.repository.StoreRepository
import kotlinx.serialization.json.Json

class StoreRepositoryImpl(
    private val storeApi: StoreApi,
    private val json: Json,
) : StoreRepository {

    override suspend fun getProducts(): ApiResult<List<Product>> {
        return safeApiCall(
            json = json,
            execute = { storeApi.getProducts() },
            mapSuccess = { body -> body.orEmpty().map { it.toDomain() } },
        )
    }

    override suspend fun purchase(productId: Long): ApiResult<PurchaseResult> {
        return safeApiCall(
            json = json,
            execute = { storeApi.purchase(PurchaseRequestDto(productId = productId)) },
            mapSuccess = { body -> body.requireBody().toDomain() },
        )
    }
}
