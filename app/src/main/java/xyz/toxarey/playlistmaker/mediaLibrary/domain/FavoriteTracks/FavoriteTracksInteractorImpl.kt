package xyz.toxarey.playlistmaker.mediaLibrary.domain.FavoriteTracks

import kotlinx.coroutines.flow.Flow
import xyz.toxarey.playlistmaker.player.domain.Track

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository):
    FavoriteTracksInteractor {
    override suspend fun insertFavoriteTrack(track: Track) {
        favoriteTracksRepository.insertFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        favoriteTracksRepository.deleteFavoriteTrack(track)
    }

    override fun checkFavoriteTrack(id: Long): Flow<Boolean> {
        return favoriteTracksRepository.checkFavoriteTrack(id)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }
}