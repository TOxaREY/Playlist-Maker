package xyz.toxarey.playlistmaker.search.domain

import xyz.toxarey.playlistmaker.player.domain.Track

interface TracksRepository {
    fun searchTracks(text: String): Result<List<Track>>
    fun getTracksFromHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun removeTracksHistory()
}