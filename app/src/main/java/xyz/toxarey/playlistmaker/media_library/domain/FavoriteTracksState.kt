package xyz.toxarey.playlistmaker.media_library.domain

import xyz.toxarey.playlistmaker.player.domain.Track

sealed interface FavoriteTracksState {
    data class Content(val tracks: List<Track>): FavoriteTracksState
    object Empty: FavoriteTracksState
}