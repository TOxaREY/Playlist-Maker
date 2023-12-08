package xyz.toxarey.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.player.domain.AudioPlayerState
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragmentViewModel(
    private val track: Track,
    private val interactorTrackMediaPlayer: TrackMediaPlayerInteractor
): ViewModel() {
    private var timerJob: Job? = null
    private val audioPlayerStateLiveData = MutableLiveData<AudioPlayerState>()
    private val updateTimerTaskLiveData = MutableLiveData<String>()

    init {
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
            AudioPlayerState.STATE_PREPARED, AudioPlayerState.STATE_PAUSED -> {
                startPlayer()
            }
            else -> {}
        }
    }

    fun pause() {
        interactorTrackMediaPlayer.pausePlayer()
        updateAudioPlayerState(AudioPlayerState.STATE_PAUSED)
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