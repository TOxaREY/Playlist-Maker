package xyz.toxarey.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.databinding.FragmentAudioPlayerBinding
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistsState
import xyz.toxarey.playlistmaker.player.domain.AudioPlayerState
import xyz.toxarey.playlistmaker.utils.EXTRA_TRACK
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment: Fragment() {
    private val viewModel: AudioPlayerFragmentViewModel by viewModel {
        parametersOf(requireArguments().getSerializable(EXTRA_TRACK))
    }
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val playlists = ArrayList<Playlist>()
    private var playlistsBottomSheetAdapter: PlaylistsBottomSheetAdapter? = null
    private var isClickAllowed = true
    private lateinit var nameSelectedPlaylist: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(
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

        viewModel.getPlaylistsStateLiveData().observe(viewLifecycleOwner) {
            playlistsState(it)
        }

        viewModel.getInsertTrackStatusLiveData().observe(viewLifecycleOwner) {
            insertTrackToPlaylistStatus(it)
        }

        binding.backToSearchButtonFromPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(
                bottomSheet: View,
                newState: Int
            ) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float
            ) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }
        })

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getFavoriteStatusLiveData().observe(viewLifecycleOwner) {
            setImageLikeButton(it)
        }

        binding.likeButton.setOnClickListener {
            setFavoriteStatus()
        }

        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.newPlaylistButtonBottomSheet.setOnClickListener {
            findNavController()
                .navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }

        setInfo(viewModel.getTrack())
        initializationAdapter()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
        setImagePlayButton()
        isClickAllowed = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylistsTracks()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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

    private fun setImageFavoriteButton() {
        binding.likeButton.setImageResource(R.drawable.ic_like_button)
        binding.likeButton.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.yp_red
            ),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun setImageNotFavoriteButton() {
        binding.likeButton.setImageResource(R.drawable.ic_not_like_button)
        binding.likeButton.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.yp_white
            ),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun setImageLikeButton(isFavorite: Boolean) {
        if (isFavorite) {
            setImageFavoriteButton()
        } else {
            setImageNotFavoriteButton()
        }
    }

    private fun setFavoriteStatus() {
        if (viewModel.getFavoriteStatusLiveData().value == true) {
            viewModel.setFavoriteStatusLiveData(false)
        } else {
            viewModel.setFavoriteStatusLiveData(true)
        }
    }

    private fun initializationAdapter() {
        playlistsBottomSheetAdapter = PlaylistsBottomSheetAdapter(playlists) { playlist ->
            if (clickDebounce()) {
                nameSelectedPlaylist = playlist.playlistName.toString()
                viewModel.setTrackToPlaylist(
                    playlist,
                    viewModel.getTrack()
                )
            }
        }
        binding.rvPlaylistsBottomSheet.adapter = playlistsBottomSheetAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun playlistsState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> {
                playlists.clear()
                playlists.addAll(state.playlists)
                playlists.reverse()
                playlistsBottomSheetAdapter?.notifyDataSetChanged()
            }
            is PlaylistsState.Empty -> {}
        }
    }

    private fun insertTrackToPlaylistStatus(isInsert: Boolean) {
        if (isInsert) {
            val toastText = getString(R.string.added_in_playlist) +
                    " " + nameSelectedPlaylist
            showToast(toastText)
            viewModel.getPlaylistsTracks()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            val toastText = getString(R.string.track_has_already_added_to_playlist) +
                    " " + nameSelectedPlaylist
            showToast(toastText)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}