package xyz.toxarey.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val ivAlbum: ImageView = itemView.findViewById(R.id.ivAlbum)
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)

    fun bind(item: Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_item_imageView_roundedCorners)))
            .placeholder(R.drawable.album_placeholder)
            .into(ivAlbum)
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
    }
}