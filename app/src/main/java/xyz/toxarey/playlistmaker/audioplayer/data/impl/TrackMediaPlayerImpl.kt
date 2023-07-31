package xyz.toxarey.playlistmaker.audioplayer.data.impl

import android.media.MediaPlayer
import xyz.toxarey.playlistmaker.audioplayer.domain.api.TrackMediaPlayer

class TrackMediaPlayerImpl(private val previewUrl: String) : TrackMediaPlayer {
    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletion.invoke()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}