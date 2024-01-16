package xyz.toxarey.playlistmaker.media_library.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long?,
    val playlistName: String?,
    val playlistDescription: String?,
    val playlistCoverPath: String?,
    val playlistTrackIdList: String?,
    val playlistTrackCount: Long = 0
)