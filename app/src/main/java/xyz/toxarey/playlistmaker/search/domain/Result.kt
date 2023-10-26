package xyz.toxarey.playlistmaker.search.domain

sealed class Result<T>(
    val data: T? = null,
    val isError: Boolean? = false
) {
    class Success<T>(data: T): Result<T>(data)
    class Error<T>(data: T? = null): Result<T>(
        data,
        true
    )
}
