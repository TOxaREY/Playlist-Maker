package xyz.toxarey.playlistmaker.search.data

import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.domain.TracksRepository

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchTrackStorage: SearchTrackStorage
): TracksRepository {
    override fun searchTracks(text: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksRequest(text))
        return when (response.resultCode) {
            -1 -> Resource.Error()
            200 -> Resource.Success((response as TracksResponse).results)
            else -> Resource.Error()
        }
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