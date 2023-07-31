package xyz.toxarey.playlistmaker.audioplayer.creator

import xyz.toxarey.playlistmaker.audioplayer.data.impl.TrackMediaPlayerImpl
import xyz.toxarey.playlistmaker.audioplayer.domain.api.TrackMediaPlayer
import xyz.toxarey.playlistmaker.audioplayer.domain.impl.TrackMediaPlayerInteractorImpl
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track
import xyz.toxarey.playlistmaker.audioplayer.presentation.api.TrackMediaPlayerInteractor
import xyz.toxarey.playlistmaker.audioplayer.presentation.presenter.AudioPlayerPresenter
import xyz.toxarey.playlistmaker.audioplayer.presentation.presenter.AudioPlayerView

object Creator {
    fun provideAudioPlayerPresenter(
        view: AudioPlayerView,
        track: Track
    ) : AudioPlayerPresenter {
        return AudioPlayerPresenter(
            view = view,
            track = track
        )
    }

    fun provideTrackMediaPlayerInteractor(previewUrl: String) : TrackMediaPlayerInteractor {
        return TrackMediaPlayerInteractorImpl(provideTrackMediaPlayer(previewUrl))
    }

    private fun provideTrackMediaPlayer(previewUrl: String) : TrackMediaPlayer {
        return TrackMediaPlayerImpl(previewUrl)
    }
}