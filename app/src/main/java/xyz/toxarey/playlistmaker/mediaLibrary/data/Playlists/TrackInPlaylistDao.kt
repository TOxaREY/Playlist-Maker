package xyz.toxarey.playlistmaker.mediaLibrary.data.Playlists

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackInPlaylistDao {
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(track: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId = :id")
    suspend fun getTrackFromPlaylist(id: Long): TrackInPlaylistEntity

    @Delete(entity = TrackInPlaylistEntity::class)
    suspend fun deleteTrackFromPlaylist(track: TrackInPlaylistEntity)
}