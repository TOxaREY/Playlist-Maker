package xyz.toxarey.playlistmaker.audioplayer.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import xyz.toxarey.playlistmaker.EXTRA_TRACK
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.audioplayer.creator.Creator
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track
import xyz.toxarey.playlistmaker.audioplayer.presentation.presenter.AudioPlayerPresenter
import xyz.toxarey.playlistmaker.audioplayer.presentation.presenter.AudioPlayerView
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity(), AudioPlayerView {
    private lateinit var presenter: AudioPlayerPresenter
    private lateinit var playButton: FloatingActionButton
    private lateinit var playbackTimeTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        playButton = findViewById(R.id.playButton)
        playbackTimeTextView = findViewById(R.id.tvPlaybackTime)
        presenter = Creator.provideAudioPlayerPresenter(this, intent.getSerializableExtra(EXTRA_TRACK) as Track)

        findViewById<Button>(R.id.back_to_search_button_from_player).setOnClickListener {
            finish()
        }

        playButton.setOnClickListener {
            presenter.playbackControl()
        }
    }
    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun setInfo(track: Track) {
        findViewById<TextView>(R.id.tvCountryValue).text = track.country
        findViewById<TextView>(R.id.tvGenreValue).text = track.primaryGenreName
        findViewById<TextView>(R.id.tvYearValue).text = track.releaseDate.split("-").toTypedArray()[0]
        if (track.collectionName != null) {
            findViewById<TextView>(R.id.tvAlbumValue).text = track.collectionName
        } else {
            findViewById<TextView>(R.id.tvAlbumValue).visibility = View.GONE
            findViewById<TextView>(R.id.tvAlbum).visibility = View.GONE
        }
        findViewById<TextView>(R.id.tvDurationValue).text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        findViewById<TextView>(R.id.tvArtistTitle).text = track.artistName
        findViewById<TextView>(R.id.tvTrackTitle).text = track.trackName
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners)))
            .placeholder(R.drawable.album_placeholder_full)
            .into(findViewById(R.id.ivAlbumFull))
    }

    override fun setImagePlayButton() {
        playButton.setImageResource(R.drawable.ic_play_button)
    }

    override fun setImagePauseButton() {
        playButton.setImageResource(R.drawable.ic_pause_button)
    }

    override fun setTextViewPlaybackTimeStart() {
        playbackTimeTextView.text = "0:00"
    }

    override fun setTextViewPlaybackTime(time: String) {
        playbackTimeTextView.text = time
    }
}