package xyz.toxarey.playlistmaker.audioplayer.domain

import android.app.Activity
import android.widget.ImageView
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track

interface GetAlbumImage {
    fun getAlbumImage(
        track: Track,
        activity: Activity,
        imageView: ImageView
    )
}