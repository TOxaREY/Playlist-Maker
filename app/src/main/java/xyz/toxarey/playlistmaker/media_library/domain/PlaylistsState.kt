package xyz.toxarey.playlistmaker.media_library.domain

sealed interface PlaylistsState {
    data class Content(val playlists: List<Playlist>): PlaylistsState
    object Empty: PlaylistsState
}