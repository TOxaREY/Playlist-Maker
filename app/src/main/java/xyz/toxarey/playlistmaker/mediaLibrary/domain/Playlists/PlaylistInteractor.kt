package xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists

import kotlinx.coroutines.flow.Flow
import xyz.toxarey.playlistmaker.player.domain.Track

interface PlaylistInteractor {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun getPlaylist(id: Long): Flow<Playlist>
    fun getPlaylists(): Flow<List<Playlist>>
    fun insertTrackToPlaylist(
        playlistId: Long,
        track: Track
    ): Flow<Boolean>
    fun getListTrackFromPlaylist(trackIds: List<Long>): Flow<List<Track>>
    fun deleteTrackFromPlaylist(
        playlistId: Long,
        track: Track
    ): Flow<Boolean>
}