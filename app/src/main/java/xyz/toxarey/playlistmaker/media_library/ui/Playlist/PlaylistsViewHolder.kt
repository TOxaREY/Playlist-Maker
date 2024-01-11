package xyz.toxarey.playlistmaker.media_library.ui.Playlist

import android.os.Environment
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.PlaylistItemBinding
import xyz.toxarey.playlistmaker.media_library.domain.Playlist
import xyz.toxarey.playlistmaker.utils.DIRECTORY_WITH_COVERS
import xyz.toxarey.playlistmaker.utils.countToString
import java.io.File

class PlaylistsViewHolder(private val binding: PlaylistItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            DIRECTORY_WITH_COVERS
        )
        val file = item.playlistCoverPath?.let {
            File(
                filePath,
                it
            )
        }
        val uri = file?.toUri()
        Glide.with(itemView.context)
            .load(uri)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners)))
            .placeholder(R.drawable.album_placeholder_full)
            .into(binding.ivPlaylistCover)
        binding.tvPlaylistName.text = item.playlistName
        binding.tvPlaylistCountTracks.text = countToString(item.playlistTrackCount)
    }
}