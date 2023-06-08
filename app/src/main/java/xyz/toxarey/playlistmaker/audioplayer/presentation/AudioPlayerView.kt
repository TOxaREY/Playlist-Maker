package xyz.toxarey.playlistmaker.audioplayer.presentation

import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track

interface AudioPlayerView {
    fun showInfo(track: Track)
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun updateTimerTask(): Runnable
}