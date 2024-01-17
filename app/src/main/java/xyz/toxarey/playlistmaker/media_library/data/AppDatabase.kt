package xyz.toxarey.playlistmaker.media_library.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        TrackInFavoriteEntity::class,
        PlaylistEntity::class,
        TrackInPlaylistEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackInFavoriteDao(): TrackInFavoriteDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao
}