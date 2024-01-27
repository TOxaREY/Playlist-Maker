package xyz.toxarey.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.toxarey.playlistmaker.databinding.PlaylistBottomSheetItemBinding
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.Playlist

class PlaylistsBottomSheetAdapter(
    private val data: ArrayList<Playlist>,
    private val onClick: (Playlist) -> Unit
): RecyclerView.Adapter<PlaylistsBottomSheetViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsBottomSheetViewHolder {
        val view = PlaylistBottomSheetItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistsBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlaylistsBottomSheetViewHolder,
        position: Int
    ) {
        val playlist = data[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onClick.invoke(playlist)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}