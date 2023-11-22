package xyz.toxarey.playlistmaker.search.domain

import xyz.toxarey.playlistmaker.player.domain.Track

sealed interface SearchState {
    object Loading: SearchState
    data class Content(val tracks: List<Track>): SearchState
    object Error: SearchState
    object Empty: SearchState
    object Default: SearchState
}