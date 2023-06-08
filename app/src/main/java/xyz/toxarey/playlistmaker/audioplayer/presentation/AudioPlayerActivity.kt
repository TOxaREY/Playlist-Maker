package xyz.toxarey.playlistmaker.audioplayer.presentation

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import xyz.toxarey.playlistmaker.EXTRA_TRACK
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track
import xyz.toxarey.playlistmaker.audioplayer.domain.models.AudioPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity(), AudioPlayerView {
    private var presenter = AudioPlayerPresenter(this)
    private var mediaPlayer = MediaPlayer()
    private var playerState = AudioPlayerState.STATE_DEFAULT
    private val runnable = updateTimerTask()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track = intent.getSerializableExtra(EXTRA_TRACK) as Track
        presenter.loadInfo(
            track,
            this,
            findViewById(R.id.ivAlbumFull)
        )
        presenter.setPlayer(track)

        findViewById<Button>(R.id.back_to_search_button_from_player).setOnClickListener {
            finish()
        }

        findViewById<FloatingActionButton>(R.id.playButton).setOnClickListener {
            playbackControl()
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
        findViewById<FloatingActionButton>(R.id.playButton).setImageResource(R.drawable.ic_play_button)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

    override fun showInfo(track: Track) {
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
    }

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = AudioPlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = AudioPlayerState.STATE_PREPARED
            findViewById<FloatingActionButton>(R.id.playButton).setImageResource(R.drawable.ic_play_button)
            findViewById<TextView>(R.id.tvPlaybackTime).text = "0:00"
            handler.removeCallbacks(runnable)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = AudioPlayerState.STATE_PLAYING
        handler.post(runnable)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = AudioPlayerState.STATE_PAUSED
        handler.removeCallbacks(runnable)
    }

    override fun playbackControl() {
        when(playerState) {
            AudioPlayerState.STATE_PLAYING -> {
                pausePlayer()
                findViewById<FloatingActionButton>(R.id.playButton).setImageResource(R.drawable.ic_play_button)
            }
            AudioPlayerState.STATE_PREPARED, AudioPlayerState.STATE_PAUSED -> {
                startPlayer()
                findViewById<FloatingActionButton>(R.id.playButton).setImageResource(R.drawable.ic_pause_button)
            }
            else -> {}
        }
    }

    override fun updateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == AudioPlayerState.STATE_PLAYING) {
                    findViewById<TextView>(R.id.tvPlaybackTime).text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, CHECkED_PLAY_TIME)
                }
            }
        }
    }
    companion object {
        private const val CHECkED_PLAY_TIME = 300L
    }
}