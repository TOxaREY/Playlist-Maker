package xyz.toxarey.playlistmaker.mediaLibrary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.toxarey.playlistmaker.mediaLibrary.data.FavoriteTracks.TrackInFavoriteDao
import xyz.toxarey.playlistmaker.mediaLibrary.data.FavoriteTracks.TrackInFavoriteEntity
import xyz.toxarey.playlistmaker.mediaLibrary.data.Playlists.PlaylistDao
import xyz.toxarey.playlistmaker.mediaLibrary.data.Playlists.PlaylistEntity
import xyz.toxarey.playlistmaker.mediaLibrary.data.Playlists.TrackInPlaylistDao
import xyz.toxarey.playlistmaker.mediaLibrary.data.Playlists.TrackInPlaylistEntity

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