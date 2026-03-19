package com.example.twochairsandroid.data.remote.api

import com.example.twochairsandroid.data.remote.dto.store.ProductDto
import com.example.twochairsandroid.data.remote.dto.store.PurchaseRequestDto
import com.example.twochairsandroid.data.remote.dto.store.PurchaseResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StoreApi {
    @GET("store/products")
    suspend fun getProducts(): Response<List<ProductDto>>

    @POST("store/purchase")
    suspend fun purchase(@Body request: PurchaseRequestDto): Response<PurchaseResponseDto>
}
