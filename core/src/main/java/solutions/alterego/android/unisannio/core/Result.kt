package solutions.alterego.android.unisannio.core

sealed class Result<out T : Any>
data class Success<out T : Any>(val data: T) : Result<T>()
data class Failure(val error: Throwable?) : Result<Nothing>()

inline fun <T : Any> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Success) action(data)
    return this
}

inline fun <T : Any> Result<T>.onError(action: (Throwable) -> Unit) {
    if (this is Failure && error != null) action(error)
}

inline fun <T : Any, R : Any> Result<T>.mapOnSuccess(map: (T) -> R) = when (this) {
    is Success -> Success(map(data))
    is Failure -> this
}
