package xyz.toxarey.playlistmaker

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(
    private val data: ArrayList<Track>,
    private val cellClickListener: CellClickListener,
    private val searchHistory: SearchHistory?
) : RecyclerView.Adapter<TracksViewHolder> () {
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val track = data[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                cellClickListener.onCellClickListener(track)
                searchHistory?.add(track)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}