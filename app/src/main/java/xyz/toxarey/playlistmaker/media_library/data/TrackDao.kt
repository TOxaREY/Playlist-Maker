package xyz.toxarey.playlistmaker.media_library.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteFavoriteTrack(track: TrackEntity)

    @Query("SELECT count(*) FROM track_table WHERE trackId == :id")
    suspend fun checkFavoriteTrack(id: Long): Int

    @Query("SELECT * FROM track_table")
    suspend fun getFavoriteTracks(): List<TrackEntity>
}