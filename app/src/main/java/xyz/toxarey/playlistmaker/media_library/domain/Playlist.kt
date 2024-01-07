package xyz.toxarey.playlistmaker.media_library.domain

data class Playlist(
    val playlistId: Long,
    val playlistName: String,
    val playlistDescription: String?,
    val playlistCoverPath: String?,
    val playlistTrackIdList: ArrayList<Long>?,
    val playlistTrackCount: Long = 0
)
