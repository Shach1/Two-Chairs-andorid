package com.example.twochairsandroid.core.network

import com.example.twochairsandroid.data.remote.dto.common.ErrorResponseDto
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.io.IOException

suspend inline fun <T, R> safeApiCall(
    json: Json,
    crossinline execute: suspend () -> Response<T>,
    crossinline mapSuccess: (T?) -> R,
): ApiResult<R> {
    return try {
        val response = execute()
        if (response.isSuccessful) {
            try {
                ApiResult.Success(mapSuccess(response.body()))
            } catch (_: EmptyBodyException) {
                ApiResult.Error(ApiError.emptyBody(response.code()))
            }
        } else {
            ApiResult.Error(
                parseApiError(
                    json = json,
                    httpCode = response.code(),
                    errorBody = response.errorBody()?.string(),
                    fallbackMessage = response.message().ifBlank { "HTTP ${response.code()}" },
                )
            )
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.network(e))
    } catch (e: Throwable) {
        ApiResult.Error(ApiError.unknown(e))
    }
}

class EmptyBodyException : IllegalStateException("Response body is empty")

fun <T> T?.requireBody(): T = this ?: throw EmptyBodyException()

fun parseApiError(
    json: Json,
    httpCode: Int,
    errorBody: String?,
    fallbackMessage: String,
): ApiError {
    if (errorBody.isNullOrBlank()) {
        return ApiError(httpCode = httpCode, message = fallbackMessage)
    }

    val parsed = runCatching { json.decodeFromString<ErrorResponseDto>(errorBody) }.getOrNull()
    return if (parsed != null) {
        ApiError(
            httpCode = httpCode,
            errorCode = parsed.errorCode,
            message = parsed.message,
            path = parsed.path,
            timestamp = parsed.timestamp,
        )
    } else {
        ApiError(httpCode = httpCode, message = fallbackMessage)
    }
}
