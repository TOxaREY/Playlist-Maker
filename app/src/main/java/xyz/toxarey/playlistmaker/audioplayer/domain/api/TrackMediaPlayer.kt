package xyz.toxarey.playlistmaker.audioplayer.domain.api

interface TrackMediaPlayer {
    fun preparePlayer(onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition() : Int
}