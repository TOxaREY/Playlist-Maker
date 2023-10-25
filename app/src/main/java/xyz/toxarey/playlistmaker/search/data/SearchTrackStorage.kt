package xyz.toxarey.playlistmaker.search.data

import xyz.toxarey.playlistmaker.player.domain.Track

interface SearchTrackStorage {
    fun read(): ArrayList<Track>
    fun add(track: Track)
    fun remove()
}