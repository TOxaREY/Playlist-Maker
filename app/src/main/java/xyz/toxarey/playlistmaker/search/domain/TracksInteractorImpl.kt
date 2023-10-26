package xyz.toxarey.playlistmaker.search.domain

import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.data.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        text: String,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            when (val resource = repository.searchTracks(text)) {
                is Resource.Success -> consumer.consume(
                    resource.data,
                    false
                )
                is Resource.Error -> consumer.consume(
                    null,
                    true
                )
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