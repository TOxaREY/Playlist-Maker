package xyz.toxarey.playlistmaker.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.domain.Result
import xyz.toxarey.playlistmaker.search.domain.TracksRepository

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchTrackStorage: SearchTrackStorage
): TracksRepository {
    override fun searchTracks(text: String): Flow<Result<List<Track>>> = flow {
        emit(networkClient.requestTracks(TracksRequest(text)))
    }

    override fun getTracksFromHistory(): ArrayList<Track> {
        return searchTrackStorage.read()
    }

    override fun addTrackToHistory(track: Track) {
        searchTrackStorage.add(track)
    }

    override fun removeTracksHistory() {
        searchTrackStorage.remove()
    }
}