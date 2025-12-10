package org.example.coins.core.domain

import org.example.coins.core.domain.Error as ErrorResult

typealias EmptyResult<E> = Result<Unit, E>

sealed interface Result<out D, out E : ErrorResult> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : ErrorResult>(val error: E) : Result<Nothing, E>
}

inline fun <T, E : ErrorResult, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

inline fun <T, E : ErrorResult> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : ErrorResult> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this

    }
}
fun <T, E : ErrorResult> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {}
}

