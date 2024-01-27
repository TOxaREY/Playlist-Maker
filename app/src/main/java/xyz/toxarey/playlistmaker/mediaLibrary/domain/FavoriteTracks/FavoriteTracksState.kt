package xyz.toxarey.playlistmaker.mediaLibrary.domain.FavoriteTracks

import xyz.toxarey.playlistmaker.player.domain.Track

sealed interface FavoriteTracksState {
    data class Content(val tracks: List<Track>): FavoriteTracksState
    object Empty: FavoriteTracksState
}