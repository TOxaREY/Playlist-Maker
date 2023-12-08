package xyz.toxarey.playlistmaker.search.domain

sealed class Result<T>(
    val data: T? = null,
    val isError: Boolean? = null
) {
    class Success<T>(data: T): Result<T>(data)
    class Error<T>(
        isError: Boolean,
        data: T? = null): Result<T>(
        data,
        isError
    )
}
