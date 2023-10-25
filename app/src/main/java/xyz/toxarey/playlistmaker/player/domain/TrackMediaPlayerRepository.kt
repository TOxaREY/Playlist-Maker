package xyz.toxarey.playlistmaker.player.domain

interface TrackMediaPlayerRepository {
    fun preparePlayer(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition() : Int
}