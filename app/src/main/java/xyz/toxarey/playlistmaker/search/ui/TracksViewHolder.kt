package xyz.toxarey.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.TrackItemBinding
import xyz.toxarey.playlistmaker.player.domain.Track
import java.text.SimpleDateFormat
import java.util.*

class TracksViewHolder(private val binding: TrackItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100.replaceAfterLast(
                '/',
                "60x60bb.jpg")
            )
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_item_imageView_roundedCorners)))
            .placeholder(R.drawable.album_placeholder)
            .into(binding.ivAlbum)
        binding.tvTrackName.text = item.trackName
        binding.tvArtistName.text = item.artistName
        binding.tvTrackTime.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(item.trackTimeMillis)
    }
}