package com.example.twochairsandroid.core.network

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(val error: ApiError) : ApiResult<Nothing>
}

data class ApiError(
    val httpCode: Int? = null,
    val errorCode: String? = null,
    val message: String,
    val path: String? = null,
    val timestamp: String? = null,
    val cause: Throwable? = null,
) {
    companion object {
        fun network(cause: Throwable? = null): ApiError = ApiError(
            message = "Network error. Check internet connection.",
            errorCode = "NETWORK_ERROR",
            cause = cause,
        )

        fun unknown(cause: Throwable? = null): ApiError = ApiError(
            message = "Unexpected error",
            errorCode = "UNKNOWN_ERROR",
            cause = cause,
        )

        fun emptyBody(httpCode: Int?): ApiError = ApiError(
            httpCode = httpCode,
            message = "Response body is empty",
            errorCode = "EMPTY_BODY",
        )
    }
}
