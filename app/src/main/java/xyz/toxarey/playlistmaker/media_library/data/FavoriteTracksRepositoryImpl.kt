package xyz.toxarey.playlistmaker.media_library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.toxarey.playlistmaker.media_library.domain.FavoriteTracksRepository
import xyz.toxarey.playlistmaker.player.domain.Track

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
): FavoriteTracksRepository {
    override suspend fun insertFavoriteTrack(track: Track) {
        appDatabase.trackDao().insertFavoriteTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        appDatabase.trackDao().deleteFavoriteTrack(trackDbConvertor.map(track))
    }

    override fun checkFavoriteTrack(id: Long): Flow<Boolean>  = flow {
        val checkFavorite = appDatabase.trackDao().checkFavoriteTrack(id)
        if (checkFavorite > 0) {
            emit(true)
        } else {
            emit(false)
        }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track ->
            trackDbConvertor.map(track)
        }
    }
}