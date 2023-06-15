package xyz.toxarey.playlistmaker.audioplayer.presentation.presenter

import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track

interface AudioPlayerView {
    fun setInfo(track: Track)
    fun setImagePlayButton()
    fun setImagePauseButton()
    fun setTextViewPlaybackTimeStart()
    fun setTextViewPlaybackTime(time: String)
}