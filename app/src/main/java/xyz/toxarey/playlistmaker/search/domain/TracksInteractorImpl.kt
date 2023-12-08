package xyz.toxarey.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.toxarey.playlistmaker.player.domain.Track

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    override fun searchTracks(text: String): Flow<QueryResult> {
        return repository.searchTracks(text).map { result ->
            when (result) {
                is Result.Success -> {
                    QueryResult(
                        result.data,
                        null
                    )
                }
                is Result.Error -> {
                    QueryResult(
                        null,
                        result.isError
                    )
                }
                else -> {}
            }
        } as Flow<QueryResult>
    }

    override fun getTracksFromHistory(): ArrayList<Track> {
        return repository.getTracksFromHistory()
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun removeTracksHistory() {
        repository.removeTracksHistory()
    }
}