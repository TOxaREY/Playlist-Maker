package xyz.toxarey.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.media_library.domain.FavoriteTracksInteractor
import xyz.toxarey.playlistmaker.media_library.domain.Playlist
import xyz.toxarey.playlistmaker.media_library.domain.PlaylistInteractor
import xyz.toxarey.playlistmaker.media_library.domain.PlaylistsState
import xyz.toxarey.playlistmaker.player.domain.AudioPlayerState
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragmentViewModel(
    private val track: Track,
    private val interactorTrackMediaPlayer: TrackMediaPlayerInteractor,
    private val interactorFavoriteTracks: FavoriteTracksInteractor,
    private val interactorPlaylist: PlaylistInteractor
): ViewModel() {
    private var timerJob: Job? = null
    private val audioPlayerStateLiveData = MutableLiveData<AudioPlayerState>()
    private val updateTimerTaskLiveData = MutableLiveData<String>()
    private val favoriteStatusLiveData = MutableLiveData<Boolean>()
    private val playlistsStateLiveData = MutableLiveData<PlaylistsState>()
    private val insertTrackStatusLiveData = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            interactorFavoriteTracks
                .checkFavoriteTrack(track.trackId)
                .collect { isFavorite ->
                    favoriteStatusLiveData.postValue(isFavorite)
                }
        }
        updateAudioPlayerState(AudioPlayerState.STATE_DEFAULT)
        interactorTrackMediaPlayer.preparePlayer(
            previewUrl = track.previewUrl,
            onPrepared = {
                updateAudioPlayerState(AudioPlayerState.STATE_PREPARED)
            },
            onCompletion = {
                updateAudioPlayerState(AudioPlayerState.STATE_PREPARED)
                updateTimerTaskLiveData.postValue("0:00")
                timerJob?.cancel()
            }
        )
        getPlaylistsTracks()
    }

    override fun onCleared() {
        super.onCleared()
        interactorTrackMediaPlayer.releasePlayer()
        timerJob?.cancel()
    }

    fun getAudioPlayerStateLiveData(): LiveData<AudioPlayerState> = audioPlayerStateLiveData

    fun getUpdateTimerTaskLiveData(): LiveData<String> = updateTimerTaskLiveData

    fun getTrack(): Track {
        return this.track
    }

    fun playbackControl() {
        when(audioPlayerStateLiveData.value) {
            AudioPlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            AudioPlayerState.STATE_PREPARED,
            AudioPlayerState.STATE_PAUSED -> {
                startPlayer()
            }
            else -> {}
        }
    }

    fun pause() {
        interactorTrackMediaPlayer.pausePlayer()
        updateAudioPlayerState(AudioPlayerState.STATE_PAUSED)
    }

    fun getFavoriteStatusLiveData(): LiveData<Boolean> = favoriteStatusLiveData

    fun setFavoriteStatusLiveData(isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                interactorFavoriteTracks.insertFavoriteTrack(track)
            } else {
                interactorFavoriteTracks.deleteFavoriteTrack(track)
            }
            favoriteStatusLiveData.postValue(isFavorite)
        }
    }

    fun getPlaylistsStateLiveData(): LiveData<PlaylistsState> = playlistsStateLiveData

    fun getPlaylistsTracks() {
        viewModelScope.launch {
            interactorPlaylist
                .getPlaylists()
                .collect { playlists ->
                    if (playlists.isEmpty()) {
                        playlistsStateLiveData.postValue(PlaylistsState.Empty)
                    } else {
                        playlistsStateLiveData.postValue(PlaylistsState.Content(playlists))
                    }
                }
        }
    }

    fun getInsertTrackStatusLiveData(): LiveData<Boolean> = insertTrackStatusLiveData
    fun setTrackToPlaylist(
        playlist: Playlist,
        track: Track
    ) {
        viewModelScope.launch {
            if (playlist.playlistTrackIdList?.contains(track.trackId) == true) {
                insertTrackStatusLiveData.postValue(false)
            } else {
                playlist.playlistId?.let {
                    interactorPlaylist.insertTrackToPlaylist(
                        it,
                        track
                    )
                        .collect { insertTrackToPlaylist ->
                            if (insertTrackToPlaylist) {
                                insertTrackStatusLiveData.postValue(true)
                            }
                        }

                }
            }
        }
    }

    private fun startPlayer() {
        interactorTrackMediaPlayer.startPlayer()
        updateAudioPlayerState(AudioPlayerState.STATE_PLAYING)
        updateTimerTask()
    }

    private fun pausePlayer() {
        interactorTrackMediaPlayer.pausePlayer()
        updateAudioPlayerState(AudioPlayerState.STATE_PAUSED)
        timerJob?.cancel()
    }

    private fun updateAudioPlayerState(state: AudioPlayerState) {
        audioPlayerStateLiveData.postValue(state)
    }

    private fun updateTimerTask() {
        timerJob = viewModelScope.launch {
            while (true) {
                updateTimerTaskLiveData.postValue(
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    )
                        .format(interactorTrackMediaPlayer.getCurrentPosition())
                )
                delay(CHECkED_PLAY_TIME)
            }
        }
    }

    companion object {
        private const val CHECkED_PLAY_TIME = 300L
    }
}