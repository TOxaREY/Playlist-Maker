package xyz.toxarey.playlistmaker.mediaLibrary.ui.Playlists

import android.os.Environment
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.PlaylistItemBinding
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.Playlist
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
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners))
            )
            .placeholder(R.drawable.album_placeholder_full)
            .into(binding.ivPlaylistCover)
        with(binding) {
            tvPlaylistName.text = item.playlistName
            tvPlaylistCountTracks.text = countToString(item.playlistTrackCount)
        }
    }
}