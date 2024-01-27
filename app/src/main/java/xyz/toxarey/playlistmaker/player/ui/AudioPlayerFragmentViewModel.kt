package xyz.toxarey.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.mediaLibrary.domain.FavoriteTracks.FavoriteTracksInteractor
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.PlaylistInteractor
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.PlaylistsState
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
    private val _audioPlayerState = MutableLiveData<AudioPlayerState>()
    private val _updateTimerTask = MutableLiveData<String>()
    private val _favoriteStatus = MutableLiveData<Boolean>()
    private val _playlistsState = MutableLiveData<PlaylistsState>()
    private val _insertTrackStatus = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            interactorFavoriteTracks
                .checkFavoriteTrack(track.trackId)
                .collect { isFavorite ->
                    _favoriteStatus.postValue(isFavorite)
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
                _updateTimerTask.postValue("0:00")
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

    val audioPlayerState: LiveData<AudioPlayerState> = _audioPlayerState

    val updateTimerTask: LiveData<String> = _updateTimerTask

    fun getTrack(): Track {
        return this.track
    }

    fun playbackControl() {
        when(_audioPlayerState.value) {
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

    val favoriteStatus: LiveData<Boolean> = _favoriteStatus

    fun setFavoriteStatusLiveData(isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                interactorFavoriteTracks.insertFavoriteTrack(track)
            } else {
                interactorFavoriteTracks.deleteFavoriteTrack(track)
            }
            _favoriteStatus.postValue(isFavorite)
        }
    }

    val playlistsState: LiveData<PlaylistsState> = _playlistsState

    fun getPlaylistsTracks() {
        viewModelScope.launch {
            interactorPlaylist
                .getPlaylists()
                .collect { playlists ->
                    if (playlists.isEmpty()) {
                        _playlistsState.postValue(PlaylistsState.Empty)
                    } else {
                        _playlistsState.postValue(PlaylistsState.Content(playlists))
                    }
                }
        }
    }

    val insertTrackStatus: LiveData<Boolean> = _insertTrackStatus
    fun setTrackToPlaylist(
        playlist: Playlist,
        track: Track
    ) {
        viewModelScope.launch {
            if (playlist.playlistTrackIdList?.contains(track.trackId) == true) {
                _insertTrackStatus.postValue(false)
            } else {
                playlist.playlistId?.let {
                    interactorPlaylist.insertTrackToPlaylist(
                        it,
                        track
                    )
                        .collect { insertTrackToPlaylist ->
                            if (insertTrackToPlaylist) {
                                _insertTrackStatus.postValue(true)
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
        _audioPlayerState.postValue(state)
    }

    private fun updateTimerTask() {
        timerJob = viewModelScope.launch {
            while (true) {
                _updateTimerTask.postValue(
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