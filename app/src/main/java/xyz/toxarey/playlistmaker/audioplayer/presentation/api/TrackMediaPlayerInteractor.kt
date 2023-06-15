package xyz.toxarey.playlistmaker.audioplayer.presentation.api

interface TrackMediaPlayerInteractor {
    fun preparePlayer(onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition() : Int
}