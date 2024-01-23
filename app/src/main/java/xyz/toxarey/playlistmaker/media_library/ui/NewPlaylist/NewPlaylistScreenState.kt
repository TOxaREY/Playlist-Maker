package xyz.toxarey.playlistmaker.media_library.ui.NewPlaylist

sealed interface NewPlaylistScreenState {
    object AllEmpty: NewPlaylistScreenState
    object FieldNameIsEmpty: NewPlaylistScreenState
    object FieldNameIsNotEmpty: NewPlaylistScreenState
}