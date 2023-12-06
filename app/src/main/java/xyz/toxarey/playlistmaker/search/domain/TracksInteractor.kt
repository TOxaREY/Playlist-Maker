package xyz.toxarey.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import xyz.toxarey.playlistmaker.player.domain.Track

interface TracksInteractor {
    fun searchTracks(text: String): Flow<Pair<List<Track>?, Boolean?>>
    fun getTracksFromHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun removeTracksHistory()
}