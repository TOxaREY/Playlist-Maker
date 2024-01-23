package xyz.toxarey.playlistmaker.media_library.domain.Playlists

data class Playlist(
    var playlistId: Long? = null,
    var playlistName: String? = null,
    var playlistDescription: String? = null,
    var playlistCoverPath: String? = null,
    var playlistTrackIdList: ArrayList<Long>? = null,
    var playlistTrackCount: Long = 0
)
