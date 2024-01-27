package xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists

sealed interface PlaylistsState {
    data class Content(val playlists: List<Playlist>): PlaylistsState
    object Empty: PlaylistsState
}