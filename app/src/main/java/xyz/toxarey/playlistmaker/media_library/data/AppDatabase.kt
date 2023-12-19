package xyz.toxarey.playlistmaker.media_library.data

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.toxarey.playlistmaker.media_library.data.TrackDao
import xyz.toxarey.playlistmaker.media_library.data.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}