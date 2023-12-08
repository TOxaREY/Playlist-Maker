package xyz.toxarey.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.toxarey.playlistmaker.databinding.TrackItemBinding
import xyz.toxarey.playlistmaker.player.domain.Track

class TracksAdapter(
    private val data: ArrayList<Track>,
    private val cellClickListener: CellClickListener
): RecyclerView.Adapter<TracksViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TracksViewHolder {
        val view = TrackItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TracksViewHolder,
        position: Int
    ) {
        val track = data[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(track)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}