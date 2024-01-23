package xyz.toxarey.playlistmaker.media_library.data.FavoriteTracks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackInFavoriteDao {
    @Insert(entity = TrackInFavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: TrackInFavoriteEntity)

    @Delete(entity = TrackInFavoriteEntity::class)
    suspend fun deleteFavoriteTrack(track: TrackInFavoriteEntity)

    @Query("SELECT count(*) FROM track_in_favorite_table WHERE trackId == :id")
    suspend fun checkFavoriteTrack(id: Long): Int

    @Query("SELECT * FROM track_in_favorite_table")
    suspend fun getFavoriteTracks(): List<TrackInFavoriteEntity>
}