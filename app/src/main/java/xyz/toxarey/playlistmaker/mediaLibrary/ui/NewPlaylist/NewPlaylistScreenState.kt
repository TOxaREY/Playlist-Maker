package xyz.toxarey.playlistmaker.mediaLibrary.ui.NewPlaylist

sealed interface NewPlaylistScreenState {
    object AllEmpty: NewPlaylistScreenState
    object FieldNameIsEmpty: NewPlaylistScreenState
    object FieldNameIsNotEmpty: NewPlaylistScreenState
}