package xyz.toxarey.playlistmaker.media_library.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun getPlaylist(id: Long): Flow<Playlist>
    fun getPlaylists(): Flow<List<Playlist>>
}