package xyz.toxarey.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.toxarey.playlistmaker.player.domain.AudioPlayerState
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val track: Track,
    private val interactorTrackMediaPlayer: TrackMediaPlayerInteractor
): ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
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
                handler.removeCallbacks(updateTimerTask())
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        interactorTrackMediaPlayer.releasePlayer()
        handler.removeCallbacks(updateTimerTask())
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
        handler.post(updateTimerTask())
    }

    private fun pausePlayer() {
        interactorTrackMediaPlayer.pausePlayer()
        updateAudioPlayerState(AudioPlayerState.STATE_PAUSED)
        handler.removeCallbacks(updateTimerTask())
    }

    private fun updateAudioPlayerState(state: AudioPlayerState) {
        audioPlayerStateLiveData.postValue(state)
    }

    private fun updateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (audioPlayerStateLiveData.value == AudioPlayerState.STATE_PLAYING) {
                    updateTimerTaskLiveData.postValue(
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        )
                            .format(interactorTrackMediaPlayer.getCurrentPosition())
                    )
                    handler.postDelayed(this, CHECkED_PLAY_TIME)
                }
            }
        }
    }

    companion object {
        private const val CHECkED_PLAY_TIME = 300L
    }
}