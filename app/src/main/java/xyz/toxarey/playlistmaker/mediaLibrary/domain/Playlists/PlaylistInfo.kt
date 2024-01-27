package xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists

import xyz.toxarey.playlistmaker.player.domain.Track

data class PlaylistInfo(
    val playlistId: Long? = null,
    val playlistName: String? = null,
    val playlistDescription: String? = null,
    val playlistCoverPath: String? = null,
    val playlistTracks: List<Track>? = null,
    val playlistTrackCount: Long = 0,
    val playlistTrackCountString: String? = null,
    val playlistDurationOfAllTracks: String? = null
)
