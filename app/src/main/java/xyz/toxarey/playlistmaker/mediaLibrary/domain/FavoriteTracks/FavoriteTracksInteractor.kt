package xyz.toxarey.playlistmaker.mediaLibrary.domain.FavoriteTracks

import kotlinx.coroutines.flow.Flow
import xyz.toxarey.playlistmaker.player.domain.Track

interface FavoriteTracksInteractor {
    suspend fun insertFavoriteTrack(track: Track)
    suspend fun deleteFavoriteTrack(track: Track)
    fun checkFavoriteTrack(id: Long): Flow<Boolean>
    fun getFavoriteTracks(): Flow<List<Track>>
}