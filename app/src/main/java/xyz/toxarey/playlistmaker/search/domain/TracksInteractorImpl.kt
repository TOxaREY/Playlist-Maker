package xyz.toxarey.playlistmaker.search.domain

import xyz.toxarey.playlistmaker.player.domain.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        text: String,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            when (val result = repository.searchTracks(text)) {
                is Result.Success -> consumer.consume(
                    result.data,
                    false
                )
                is Result.Error -> consumer.consume(
                    null,
                    true
                )

                else -> {}
            }
        }
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