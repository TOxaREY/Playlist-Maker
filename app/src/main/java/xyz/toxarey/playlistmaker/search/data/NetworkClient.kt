package xyz.toxarey.playlistmaker.search.data

interface NetworkClient {
    fun doRequest(request: Any): Response
}