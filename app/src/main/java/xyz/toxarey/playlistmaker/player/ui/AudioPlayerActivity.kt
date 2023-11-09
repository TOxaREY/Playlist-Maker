package xyz.toxarey.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.databinding.ActivityAudioPlayerBinding
import xyz.toxarey.playlistmaker.player.domain.AudioPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(intent)
    }
    private lateinit var binding: ActivityAudioPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getAudioPlayerStateLiveData().observe(this) {
            setImagePlayPauseButton(it)
        }

        viewModel.getUpdateTimerTaskLiveData().observe(this) {
            setTextViewPlaybackTime(it)
        }

        binding.backToSearchButtonFromPlayer.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        setInfo(viewModel.getTrack())
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
        setImagePlayButton()
    }

    private fun setInfo(track: Track) {
        binding.apply {
            tvCountryValue.text = track.country
            tvGenreValue.text = track.primaryGenreName
            tvYearValue.text = track.releaseDate.split("-").toTypedArray()[0]
            if (track.collectionName != null) {
                tvAlbumValue.text = track.collectionName
            } else {
                tvAlbumValue.visibility = View.GONE
                tvAlbum.visibility = View.GONE
            }
            tvDurationValue.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis)
            tvArtistTitle.text = track.artistName
            tvTrackTitle.text = track.trackName
        }
        Glide.with(this)
            .load(
                track.artworkUrl100.replaceAfterLast(
                    '/',
                    "512x512bb.jpg"
                )
            )
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners)))
            .placeholder(R.drawable.album_placeholder_full)
            .into(findViewById(R.id.ivAlbumFull))
    }

    private fun setImagePlayPauseButton(state: AudioPlayerState) {
        when(state) {
            AudioPlayerState.STATE_PLAYING -> {
                setImagePauseButton()
            }
            AudioPlayerState.STATE_PREPARED, AudioPlayerState.STATE_PAUSED -> {
                setImagePlayButton()
            }
            else -> {}
        }
    }

    private fun setImagePlayButton() {
        binding.playButton.setImageResource(R.drawable.ic_play_button)
    }

    private fun setImagePauseButton() {
        binding.playButton.setImageResource(R.drawable.ic_pause_button)
    }

    private fun setTextViewPlaybackTime(time: String) {
        binding.tvPlaybackTime.text = time
    }
}