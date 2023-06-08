package xyz.toxarey.playlistmaker.audioplayer.presentation

import android.app.Activity
import android.widget.ImageView
import xyz.toxarey.playlistmaker.audioplayer.data.GetAlbumImageImpl
import xyz.toxarey.playlistmaker.audioplayer.domain.SetAlbumImage
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track

class AudioPlayerPresenter(
    private val view: AudioPlayerView
    ) {

    private val getAlbumImage = GetAlbumImageImpl()
    private val setAlbumImage = SetAlbumImage(getAlbumImage)

    fun loadInfo(track: Track, activity: Activity, imageView: ImageView) {
        view.showInfo(track)
        setAlbumImage.execute(
            track,
            activity,
            imageView
        )
    }

    fun setPlayer(track: Track) {
        view.preparePlayer(track)
    }
}