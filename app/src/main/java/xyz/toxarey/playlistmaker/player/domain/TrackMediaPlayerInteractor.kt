package xyz.toxarey.playlistmaker.player.domain

interface TrackMediaPlayerInteractor {
    fun preparePlayer(
        previewUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition() : Int
}