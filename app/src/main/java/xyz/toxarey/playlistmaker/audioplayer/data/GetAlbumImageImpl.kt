package xyz.toxarey.playlistmaker.audioplayer.data

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.audioplayer.domain.GetAlbumImage
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track

class GetAlbumImageImpl: GetAlbumImage {
    override fun getAlbumImage(
        track: Track,
        activity: Activity,
        imageView: ImageView
    ) {
        Glide.with(activity)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(activity.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners)))
            .placeholder(R.drawable.album_placeholder_full)
            .into(imageView)
    }
}