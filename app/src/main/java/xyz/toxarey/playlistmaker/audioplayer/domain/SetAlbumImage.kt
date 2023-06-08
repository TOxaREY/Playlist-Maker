package xyz.toxarey.playlistmaker.audioplayer.domain

import android.app.Activity
import android.widget.ImageView
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track

class SetAlbumImage(
    private val getAlbumImage: GetAlbumImage
) {
    fun execute(
        track: Track,
        activity: Activity,
        imageView: ImageView
    ) {
        getAlbumImage.getAlbumImage(
            track,
            activity,
            imageView
        )
    }
}