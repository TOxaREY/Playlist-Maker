package xyz.toxarey.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.databinding.FragmentAudioPlayerBinding
import xyz.toxarey.playlistmaker.player.domain.AudioPlayerState
import xyz.toxarey.playlistmaker.utils.EXTRA_TRACK
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment: Fragment() {
    private val viewModel: AudioPlayerFragmentViewModel by viewModel {
        parametersOf(requireArguments().getSerializable(EXTRA_TRACK))
    }
    private lateinit var binding: FragmentAudioPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioPlayerBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        viewModel.getAudioPlayerStateLiveData().observe(viewLifecycleOwner) {
            setImagePlayPauseButton(it)
        }

        viewModel.getUpdateTimerTaskLiveData().observe(viewLifecycleOwner) {
            setTextViewPlaybackTime(it)
        }

        binding.backToSearchButtonFromPlayer.setOnClickListener {
            findNavController().navigateUp()
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
            .into(binding.ivAlbumFull)
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