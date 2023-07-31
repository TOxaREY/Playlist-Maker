package xyz.toxarey.playlistmaker.audioplayer.domain.impl

import xyz.toxarey.playlistmaker.audioplayer.domain.api.TrackMediaPlayer
import xyz.toxarey.playlistmaker.audioplayer.presentation.api.TrackMediaPlayerInteractor

class TrackMediaPlayerInteractorImpl(private val trackMediaPlayer: TrackMediaPlayer) :
    TrackMediaPlayerInteractor {

    override fun preparePlayer(onPrepared: () -> Unit, onCompletion: () -> Unit) {
        trackMediaPlayer.preparePlayer(onPrepared, onCompletion)
    }

    override fun startPlayer() {
        trackMediaPlayer.startPlayer()
    }

    override fun pausePlayer() {
        trackMediaPlayer.pausePlayer()
    }

    override fun releasePlayer() {
        trackMediaPlayer.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return trackMediaPlayer.getCurrentPosition()
    }
}