package xyz.toxarey.playlistmaker.search.domain

import xyz.toxarey.playlistmaker.player.domain.Track

interface TracksInteractor {
    fun searchTracks(
        text: String,
        consumer: TracksConsumer
    )
    fun getTracksFromHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun removeTracksHistory()

    interface TracksConsumer {
        fun consume(
            tracks: List<Track>?,
            isError: Boolean?
        )
    }
}