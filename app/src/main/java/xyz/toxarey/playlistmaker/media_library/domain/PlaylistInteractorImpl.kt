package xyz.toxarey.playlistmaker.media_library.domain

import kotlinx.coroutines.flow.Flow
import xyz.toxarey.playlistmaker.player.domain.Track

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistRepository.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override fun getPlaylist(id: Long): Flow<Playlist> {
        return playlistRepository.getPlaylist(id)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun insertTrackToPlaylist(
        playlistId: Long,
        track: Track
    ): Flow<Boolean> {
        return playlistRepository.insertTrackToPlaylist(
            playlistId,
            track
        )
    }

}