package xyz.toxarey.playlistmaker.media_library.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface TrackInPlaylistDao {
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(track: TrackInPlaylistEntity)
}