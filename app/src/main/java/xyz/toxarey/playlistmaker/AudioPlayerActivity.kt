package xyz.toxarey.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
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
    private lateinit var playButton: FloatingActionButton
    private lateinit var playbackTimeTextView: TextView
    private lateinit var url: String
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        handler = Handler(Looper.getMainLooper())
        val track = intent.getSerializableExtra(EXTRA_TRACK) as Track
        addViews()
        setInfo(track)
        preparePlayer()

        backToSearchButton.setOnClickListener {
            finish()
        }

        playButton.setOnClickListener {
            playbackControl()
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
        playButton.setImageResource(R.drawable.ic_play_button)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimerTask())
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
        playButton = findViewById(R.id.playButton)
        playbackTimeTextView = findViewById(R.id.tvPlaybackTime)
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
        url = trk.previewUrl
        Glide.with(this)
            .load(trk.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners)))
            .placeholder(R.drawable.album_placeholder_full)
            .into(albumImageView)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playButton.setImageResource(R.drawable.ic_play_button)
            playbackTimeTextView.text = "0:00"
            handler.removeCallbacks(updateTimerTask())
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        handler.post(updateTimerTask())
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateTimerTask())
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                playButton.setImageResource(R.drawable.ic_play_button)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                playButton.setImageResource(R.drawable.ic_pause_button)
            }

        }
    }

    private fun updateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    playbackTimeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, CHECkED_PLAY_TIME)
                }
            }
        }
    }
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val CHECkED_PLAY_TIME = 300L
    }
}