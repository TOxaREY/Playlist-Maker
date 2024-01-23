package xyz.toxarey.playlistmaker.media_library.domain.Playlists

import xyz.toxarey.playlistmaker.player.domain.Track

data class PlaylistInfo(
    var playlistId: Long? = null,
    var playlistName: String? = null,
    var playlistDescription: String? = null,
    var playlistCoverPath: String? = null,
    var playlistTracks: List<Track>? = null,
    var playlistTrackCount: String? = null,
    var playlistDurationOfAllTracks: String? = null
)
