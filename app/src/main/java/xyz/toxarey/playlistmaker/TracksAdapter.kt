package xyz.toxarey.playlistmaker

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(
    private val data: ArrayList<Track>,
    private val searchHistory: SearchHistory?
) : RecyclerView.Adapter<TracksViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            val activity = holder.itemView.context as Activity
            val audioPlayerIntent = Intent(activity, AudioPlayerActivity::class.java)
            audioPlayerIntent.putExtra(EXTRA_TRACK, data[position])
            startActivity(activity, audioPlayerIntent, null)
            searchHistory?.add(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}