package xyz.toxarey.playlistmaker.media_library.data.FavoriteTracks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.toxarey.playlistmaker.media_library.data.AppDatabase
import xyz.toxarey.playlistmaker.media_library.data.Playlists.TrackDbConvertor
import xyz.toxarey.playlistmaker.media_library.domain.FavoriteTracks.FavoriteTracksRepository
import xyz.toxarey.playlistmaker.player.domain.Track

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
): FavoriteTracksRepository {
    override suspend fun insertFavoriteTrack(track: Track) {
        appDatabase.trackInFavoriteDao().insertFavoriteTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        appDatabase.trackInFavoriteDao().deleteFavoriteTrack(trackDbConvertor.map(track))
    }

    override fun checkFavoriteTrack(id: Long): Flow<Boolean>  = flow {
        val checkFavorite = appDatabase.trackInFavoriteDao().checkFavoriteTrack(id)
        if (checkFavorite > 0) {
            emit(true)
        } else {
            emit(false)
        }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackInFavoriteDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackInFavoriteEntity>): List<Track> {
        return tracks.map { track ->
            trackDbConvertor.map(track)
        }
    }
}