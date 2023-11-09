package xyz.toxarey.playlistmaker.player.data

import android.media.MediaPlayer
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerRepository

class TrackMediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer): TrackMediaPlayerRepository {
    override fun preparePlayer(
        previewUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { onPrepared.invoke() }
        mediaPlayer.setOnCompletionListener { onCompletion.invoke() }
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