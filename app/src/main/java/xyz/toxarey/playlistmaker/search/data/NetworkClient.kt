package xyz.toxarey.playlistmaker.search.data

import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.domain.Result

interface NetworkClient {
    suspend fun requestTracks(request: TracksRequest): Result<List<Track>>
}