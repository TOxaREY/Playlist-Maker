package xyz.toxarey.playlistmaker.search.domain

import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.data.Resource

interface TracksRepository {
    fun searchTracks(text: String): Resource<List<Track>>
    fun getTracksFromHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun removeTracksHistory()
}