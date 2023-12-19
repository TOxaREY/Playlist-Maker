package xyz.toxarey.playlistmaker.media_library.domain

import kotlinx.coroutines.flow.Flow
import xyz.toxarey.playlistmaker.player.domain.Track

interface FavoriteTracksRepository {
    suspend fun insertFavoriteTrack(track: Track)
    suspend fun deleteFavoriteTrack(track: Track)
    fun checkFavoriteTrack(id: Long): Flow<Boolean>
    fun getFavoriteTracks(): Flow<List<Track>>
}