package xyz.toxarey.playlistmaker.media_library.ui.NewPlaylist

import xyz.toxarey.playlistmaker.media_library.domain.FavoriteTracksState

sealed interface NewPlaylistScreenState {
    object AllEmpty: NewPlaylistScreenState
    object FieldNameIsEmpty: NewPlaylistScreenState
    object FieldNameIsNotEmpty: NewPlaylistScreenState
}