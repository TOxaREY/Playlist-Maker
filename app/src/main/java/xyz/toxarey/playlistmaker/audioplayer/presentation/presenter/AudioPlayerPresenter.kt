package xyz.toxarey.playlistmaker.audioplayer.presentation.presenter

import android.os.Handler
import android.os.Looper
import xyz.toxarey.playlistmaker.audioplayer.creator.Creator
import xyz.toxarey.playlistmaker.audioplayer.domain.models.AudioPlayerState
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerPresenter(
    private var view: AudioPlayerView,
    private val track: Track
) {
    private val interactorTrackMediaPlayer = Creator.provideTrackMediaPlayerInteractor(track.previewUrl)
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = updateTimerTask()
    private var playerState = AudioPlayerState.STATE_DEFAULT

    init {
        view.setInfo(track)
        interactorTrackMediaPlayer.preparePlayer(
            onPrepared = {
                playerState = AudioPlayerState.STATE_PREPARED
            },
            onCompletion = {
                view.setImagePlayButton()
                view.setTextViewPlaybackTimeStart()
                playerState = AudioPlayerState.STATE_PREPARED
                handler.removeCallbacks(runnable)
            }
        )
    }

    fun playbackControl() {
        when(playerState) {
            AudioPlayerState.STATE_PLAYING -> {
                pausePlayer()
                view.setImagePlayButton()
            }
            AudioPlayerState.STATE_PREPARED, AudioPlayerState.STATE_PAUSED -> {
                startPlayer()
                view.setImagePauseButton()
            }
            else -> {}
        }
    }

    fun pause() {
        interactorTrackMediaPlayer.pausePlayer()
        view.setImagePlayButton()
    }

    fun destroy() {
        interactorTrackMediaPlayer.releasePlayer()
        handler.removeCallbacks(runnable)
    }

    private fun startPlayer() {
        interactorTrackMediaPlayer.startPlayer()
        playerState = AudioPlayerState.STATE_PLAYING
        handler.post(runnable)
    }

    private fun pausePlayer() {
        interactorTrackMediaPlayer.pausePlayer()
        playerState = AudioPlayerState.STATE_PAUSED
        handler.removeCallbacks(runnable)
    }

    private fun updateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == AudioPlayerState.STATE_PLAYING) {
                    view.setTextViewPlaybackTime(SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(interactorTrackMediaPlayer.getCurrentPosition()))
                    handler.postDelayed(this, CHECkED_PLAY_TIME)
                }
            }
        }
    }

    companion object {
        private const val CHECkED_PLAY_TIME = 300L
    }
}