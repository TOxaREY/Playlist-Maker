package xyz.toxarey.playlistmaker.search.data
sealed class Resource<T>(
    val data: T? = null,
    val isError: Boolean? = false
) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(data: T? = null): Resource<T>(
        data,
        true
    )
}