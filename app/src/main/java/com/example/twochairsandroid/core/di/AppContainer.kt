package com.example.twochairsandroid.core.di

import android.content.Context
import com.example.twochairsandroid.BuildConfig
import com.example.twochairsandroid.core.network.AuthHeaderInterceptor
import com.example.twochairsandroid.core.storage.TokenStorage
import com.example.twochairsandroid.data.remote.api.AuthApi
import com.example.twochairsandroid.data.remote.api.DeckApi
import com.example.twochairsandroid.data.remote.api.GameApi
import com.example.twochairsandroid.data.remote.api.MyDeckApi
import com.example.twochairsandroid.data.remote.api.StoreApi
import com.example.twochairsandroid.data.repository.AuthRepositoryImpl
import com.example.twochairsandroid.data.repository.DeckRepositoryImpl
import com.example.twochairsandroid.data.repository.GameRepositoryImpl
import com.example.twochairsandroid.data.repository.MyDeckRepositoryImpl
import com.example.twochairsandroid.data.repository.StoreRepositoryImpl
import com.example.twochairsandroid.domain.repository.AuthRepository
import com.example.twochairsandroid.domain.repository.DeckRepository
import com.example.twochairsandroid.domain.repository.GameRepository
import com.example.twochairsandroid.domain.repository.MyDeckRepository
import com.example.twochairsandroid.domain.repository.StoreRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    val json: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val tokenStorage = TokenStorage(context)

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.BASIC
        }
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(AuthHeaderInterceptor(tokenStorage))
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(normalizeBaseUrl(BuildConfig.API_BASE_URL))
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    private val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    private val deckApi: DeckApi = retrofit.create(DeckApi::class.java)
    private val gameApi: GameApi = retrofit.create(GameApi::class.java)
    private val storeApi: StoreApi = retrofit.create(StoreApi::class.java)
    private val myDeckApi: MyDeckApi = retrofit.create(MyDeckApi::class.java)

    val authRepository: AuthRepository = AuthRepositoryImpl(
        authApi = authApi,
        tokenStorage = tokenStorage,
        json = json,
    )

    val deckRepository: DeckRepository = DeckRepositoryImpl(
        deckApi = deckApi,
        json = json,
    )

    val gameRepository: GameRepository = GameRepositoryImpl(
        gameApi = gameApi,
        json = json,
    )

    val storeRepository: StoreRepository = StoreRepositoryImpl(
        storeApi = storeApi,
        json = json,
    )

    val myDeckRepository: MyDeckRepository = MyDeckRepositoryImpl(
        myDeckApi = myDeckApi,
        json = json,
    )

    private fun normalizeBaseUrl(url: String): String {
        return if (url.endsWith('/')) url else "$url/"
    }
}
