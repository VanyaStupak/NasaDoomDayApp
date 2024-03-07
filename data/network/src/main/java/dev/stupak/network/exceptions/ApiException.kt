package dev.stupak.network.exceptions

sealed class ApiException(
    open val originException: Exception,
) : Exception()

class TokenExpiredApiException(
    val errorCode: Int,
    val errorMessage: String,
    override val originException: Exception,
) : ApiException(originException)

class NotFoundApiException(
    val errorCode: Int,
    val errorMessage: String,
    override val originException: Exception,
) : ApiException(originException)

class InternalServerApiException(
    val errorCode: Int,
    val errorMessage: String,
    override val originException: Exception,
) : ApiException(originException)

class ServiceUnavailableApiException(
    val errorCode: Int,
    val errorMessage: String,
    override val originException: Exception,
) : ApiException(originException)

class UnknownApiException(
    override val originException: Exception,
) : ApiException(originException)
