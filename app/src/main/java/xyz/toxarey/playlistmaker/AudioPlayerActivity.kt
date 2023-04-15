package xyz.toxarey.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var backToSearchButton: Button
    private lateinit var countryTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var albumTextView: TextView
    private lateinit var albumTextViewTitle: TextView
    private lateinit var durationTextView: TextView
    private lateinit var artistTextView: TextView
    private lateinit var trackTextView: TextView
    private lateinit var albumImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track = intent.getSerializableExtra(EXTRA_TRACK) as Track
        addViews()
        setInfo(track)

        backToSearchButton.setOnClickListener {
            finish()
        }
    }

    private fun addViews() {
        backToSearchButton = findViewById(R.id.back_to_search_button_from_player)
        countryTextView = findViewById(R.id.tvCountryValue)
        genreTextView = findViewById(R.id.tvGenreValue)
        yearTextView = findViewById(R.id.tvYearValue)
        albumTextView = findViewById(R.id.tvAlbumValue)
        albumTextViewTitle = findViewById(R.id.tvAlbum)
        durationTextView = findViewById(R.id.tvDurationValue)
        artistTextView = findViewById(R.id.tvArtistTitle)
        trackTextView = findViewById(R.id.tvTrackTitle)
        albumImageView = findViewById(R.id.ivAlbumFull)
    }

    private fun setInfo(trk: Track) {
        countryTextView.text = trk.country
        genreTextView.text = trk.primaryGenreName
        yearTextView.text = trk.releaseDate.split("-").toTypedArray()[0]
        if (trk.collectionName != null) {
            albumTextView.text = trk.collectionName
        } else {
            albumTextView.visibility = View.GONE
            albumTextViewTitle.visibility = View.GONE
        }
        durationTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trk.trackTimeMillis)
        artistTextView.text = trk.artistName
        trackTextView.text = trk.trackName
        Glide.with(this)
            .load(trk.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners)))
            .placeholder(R.drawable.album_placeholder_full)
            .into(albumImageView)
    }
}