package xyz.toxarey.playlistmaker.media_library.domain.Playlists

sealed interface PlaylistsState {
    data class Content(val playlists: List<Playlist>): PlaylistsState
    object Empty: PlaylistsState
}