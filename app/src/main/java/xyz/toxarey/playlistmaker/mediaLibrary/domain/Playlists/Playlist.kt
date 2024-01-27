package xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists

data class Playlist(
    val playlistId: Long? = null,
    val playlistName: String? = null,
    val playlistDescription: String? = null,
    val playlistCoverPath: String? = null,
    val playlistTrackIdList: ArrayList<Long>? = null,
    val playlistTrackCount: Long = 0
) : java.io.Serializable
