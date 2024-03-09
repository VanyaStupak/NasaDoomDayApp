package dev.stupak.source

import dev.stupak.network.exceptions.ApiException
import dev.stupak.network.exceptions.InternalServerApiException
import dev.stupak.network.exceptions.NotFoundApiException
import dev.stupak.network.exceptions.ServiceUnavailableApiException
import dev.stupak.network.exceptions.TokenExpiredApiException
import dev.stupak.network.exceptions.UnknownApiException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.HttpURLConnection
import kotlin.jvm.Throws

interface BaseNetSource {
    @Throws(ApiException::class)
    suspend fun <T> apiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> T,
    ): T {
        return withContext(dispatcher) {
            try {
                call()
            } catch (ex: Exception) {
                when (ex) {
                    is HttpException -> {
                        when (ex.code()) {
                            HttpURLConnection.HTTP_UNAUTHORIZED -> throw TokenExpiredApiException(ex.code(), ex.message(), ex)
                            HttpURLConnection.HTTP_NOT_FOUND -> throw NotFoundApiException(ex.code(), ex.message(), ex)
                            HttpURLConnection.HTTP_INTERNAL_ERROR -> throw InternalServerApiException(ex.code(), ex.message(), ex)
                            HttpURLConnection.HTTP_UNAVAILABLE -> throw ServiceUnavailableApiException(ex.code(), ex.message(), ex)
                            else -> throw UnknownApiException(ex)
                        }
                    }
                    else -> throw UnknownApiException(ex)
                }
            }
        }
    }
}
