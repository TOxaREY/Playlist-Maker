package xyz.toxarey.playlistmaker.media_library.ui.Playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.toxarey.playlistmaker.databinding.PlaylistItemBinding
import xyz.toxarey.playlistmaker.media_library.domain.Playlist

class PlaylistsAdapter(
    private val data: ArrayList<Playlist>,
    private val onClick: (Playlist) -> Unit
): RecyclerView.Adapter<PlaylistsViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsViewHolder {
        val view = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlaylistsViewHolder,
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