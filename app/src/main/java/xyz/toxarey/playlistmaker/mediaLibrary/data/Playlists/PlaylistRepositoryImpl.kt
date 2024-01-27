package xyz.toxarey.playlistmaker.mediaLibrary.data.Playlists

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.toxarey.playlistmaker.mediaLibrary.data.AppDatabase
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.PlaylistRepository
import xyz.toxarey.playlistmaker.player.domain.Track

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
): PlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlistDbConvertor.map(playlist))
        getPlaylists().collect { playlists ->
            if (playlists.isNotEmpty()) {
                if (playlist.playlistTrackIdList != null) {
                    playlist.playlistTrackIdList.forEach {
                        val track = playlistDbConvertor.map(appDatabase.trackInPlaylistDao().getTrackFromPlaylist(it))
                        if (isTrackNotIncludedInPlaylists(
                                playlists,
                                track
                            )
                        ) {
                            appDatabase.trackInPlaylistDao()
                                .deleteTrackFromPlaylist(playlistDbConvertor.map(track))
                        }
                    }
                }
            } else {
                if (playlist.playlistTrackIdList != null) {
                    playlist.playlistTrackIdList.forEach {
                        val track = playlistDbConvertor.map(appDatabase.trackInPlaylistDao().getTrackFromPlaylist(it))
                        appDatabase.trackInPlaylistDao().deleteTrackFromPlaylist(playlistDbConvertor.map(track))
                    }
                }
            }
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylist(id: Long): Flow<Playlist> = flow {
        emit(playlistDbConvertor.map(appDatabase.playlistDao().getPlaylist(id)))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun insertTrackToPlaylist(
        playlistId: Long,
        track: Track
    ): Flow<Boolean> = flow {
        getPlaylist(playlistId).collect { playlist ->
            var playlistCopy = playlist
            appDatabase.trackInPlaylistDao().insertTrackToPlaylist(playlistDbConvertor.map(track))
            if (playlistCopy.playlistTrackIdList == null) {
                playlistCopy = playlistCopy.copy(playlistTrackIdList = ArrayList())
            }
            playlistCopy.playlistTrackIdList!!.add(track.trackId)
            playlistCopy = playlistCopy.copy(playlistTrackCount = playlistCopy.playlistTrackCount.plus(1))
            appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlistCopy))
            getPlaylist(playlistId).collect { updatedPlaylist ->
                if (updatedPlaylist.playlistTrackIdList?.contains(track.trackId) == true) {
                    emit(true)
                } else {
                    emit(false)
                }
            }
        }
    }

    override fun getListTrackFromPlaylist(trackIds: List<Long>): Flow<List<Track>> = flow {
        emit(trackIds.map { id ->
            playlistDbConvertor.map(appDatabase.trackInPlaylistDao().getTrackFromPlaylist(id))
        })
    }

    override fun deleteTrackFromPlaylist(
        playlistId: Long,
        track: Track
    ): Flow<Boolean> = flow {
        getPlaylist(playlistId).collect { playlist ->
            var playlistCopy = playlist
            playlistCopy.playlistTrackIdList!!.remove(track.trackId)
            playlistCopy = playlistCopy.copy(playlistTrackCount = playlistCopy.playlistTrackCount.minus(1))
            appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlistCopy))
            getPlaylists().collect { playlists ->
                if (isTrackNotIncludedInPlaylists(
                    playlists,
                    track
                )) {
                    appDatabase.trackInPlaylistDao().deleteTrackFromPlaylist(playlistDbConvertor.map(track))
                }
                getPlaylist(playlistId).collect { updatedPlaylist ->
                    if (updatedPlaylist.playlistTrackIdList?.contains(track.trackId) == true) {
                        emit(false)
                    } else {
                        emit(true)
                    }
                }
            }
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist ->
            playlistDbConvertor.map(playlist)
        }
    }

    private fun isTrackNotIncludedInPlaylists(
        playlists: List<Playlist>,
        track: Track
    ): Boolean {
        var isTrackNotIncludedInPlaylists = true
        playlists.forEach { playlist ->
            if (playlist.playlistTrackIdList?.contains(track.trackId) == true) {
                isTrackNotIncludedInPlaylists = false
                return@forEach
            }
        }
        return isTrackNotIncludedInPlaylists
    }
}