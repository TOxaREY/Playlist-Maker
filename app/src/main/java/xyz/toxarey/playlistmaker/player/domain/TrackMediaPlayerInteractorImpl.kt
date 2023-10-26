package xyz.toxarey.playlistmaker.player.domain

class TrackMediaPlayerInteractorImpl(private val trackMediaPlayerRepository: TrackMediaPlayerRepository) :
    TrackMediaPlayerInteractor {

    override fun preparePlayer(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        trackMediaPlayerRepository.preparePlayer(
            onPrepared,
            onCompletion
        )
    }

    override fun startPlayer() {
        trackMediaPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        trackMediaPlayerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        trackMediaPlayerRepository.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return trackMediaPlayerRepository.getCurrentPosition()
    }
}