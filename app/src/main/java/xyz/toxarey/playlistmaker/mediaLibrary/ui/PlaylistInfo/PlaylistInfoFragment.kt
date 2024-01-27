package xyz.toxarey.playlistmaker.mediaLibrary.ui.PlaylistInfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.FragmentPlaylistInfoBinding
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.PlaylistInfo
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.player.ui.PlaylistsBottomSheetAdapter
import xyz.toxarey.playlistmaker.utils.EDITABLE_PLAYLIST
import xyz.toxarey.playlistmaker.utils.EXTRA_TRACK
import xyz.toxarey.playlistmaker.utils.PLAYLIST_ID

class PlaylistInfoFragment: Fragment() {
    private val viewModel: PlaylistInfoFragmentViewModel by viewModel {
        parametersOf(requireArguments().getLong(PLAYLIST_ID))
    }
    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!
    private var tracksBottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var menuBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private var tracksBottomSheetAdapter: TracksPlaylistInfoAdapter? = null
    private var menuBottomSheetAdapter: PlaylistsBottomSheetAdapter? = null
    private var listTrack = ArrayList<Track>()
    private var isClickAllowed = true
    private var playlist = ArrayList<Playlist>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistInfoBinding.inflate(
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
        viewModel.getPlaylistInfo.observe(viewLifecycleOwner) { playlistInfo ->
            updatePlaylistInfo(playlistInfo)
        }

        initializationAdapters()
        initializationButtonsListener()
        initializationBottomSheetBehaviors()
        viewModel.getPlaylist()
    }

    override fun onPause() {
        super.onPause()
        isClickAllowed = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylist()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatePlaylistInfo(playlistInfo: PlaylistInfo) {
        if (!playlistInfo.playlistCoverPath.isNullOrEmpty()) {
            binding.ivPlaylistCoverFull.setPadding(
                0,
                0,
                0,
                0
            )
            Glide.with(this)
                .load(viewModel.getPlaylistCoverUri(requireContext()))
                .centerCrop()
                .placeholder(R.drawable.album_placeholder_full)
                .into(binding.ivPlaylistCoverFull)
        }
        with(binding) {
            tvPlaylistInfoName.text = playlistInfo.playlistName
            tvPlaylistInfoDescription.text = playlistInfo.playlistDescription
            tvPlaylistInfoTime.text = playlistInfo.playlistDurationOfAllTracks
            tvPlaylistInfoCountTracks.text = playlistInfo.playlistTrackCountString
            listTrack.clear()
            listTrack.addAll(ArrayList(playlistInfo.playlistTracks))
            listTrack.reverse()
            playlist.clear()
            playlist.add(viewModel.getPlaylistFromDb())
            tracksBottomSheetAdapter?.notifyDataSetChanged()
            menuBottomSheetAdapter?.notifyDataSetChanged()
            if (playlistInfo.playlistTrackCount == 0L) {
                tvNotTracksInPlaylist.isVisible = true
            }
        }
    }

    private fun initializationAdapters() {
        tracksBottomSheetAdapter = TracksPlaylistInfoAdapter(listTrack) { track, isLong ->
            if (clickDebounce()) {
                if (isLong) {
                    showConfirmDialogDeleteTrack(track)
                } else {
                    segueToAudioPlayerFragment(track)
                }
            }
        }
        binding.rvListTrackBottomSheet.adapter = tracksBottomSheetAdapter

        menuBottomSheetAdapter = PlaylistsBottomSheetAdapter(playlist) {}
        binding.rvMenuBottomSheet.adapter = menuBottomSheetAdapter
    }

    private fun initializationButtonsListener() {
        with(binding) {
            backToMediaLibraryButtonFromPlaylistInfo.setOnClickListener {
                findNavController().navigateUp()
            }

            ivPlaylistInfoShare.setOnClickListener {
                sharePlaylist()
            }

            ivPlaylistInfoMore.setOnClickListener {
                menuBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            tvShare.setOnClickListener {
                sharePlaylist()
            }

            tvEditingInfo.setOnClickListener {
                findNavController().navigate(
                    R.id.action_playlistInfoFragment_to_editingPlaylistFragment,
                    bundleOf(EDITABLE_PLAYLIST to viewModel.getPlaylistFromDb())
                )
            }

            tvDeletePlaylist.setOnClickListener {
                menuBottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                showConfirmDialogDeletePlaylist(viewModel.getPlaylistFromDb())
            }
        }
    }

    private fun initializationBottomSheetBehaviors() {
        tracksBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
        tracksBottomSheetBehavior?.peekHeight =
            resources.displayMetrics.heightPixels -
                    resources.displayMetrics.widthPixels -
                    resources.getDimensionPixelSize(R.dimen.sum_height_items_playlist_info)

        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        menuBottomSheetBehavior?.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(
                bottomSheet: View,
                newState: Int
            ) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
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
    }

    private fun segueToAudioPlayerFragment(track: Track) {
        findNavController().navigate(
            R.id.action_playlistInfoFragment_to_audioPlayerFragment,
            bundleOf(EXTRA_TRACK to track)
        )
    }

    private fun showConfirmDialogDeleteTrack(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.want_to_delete_a_track)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track)
            }.show()
    }

    private fun showConfirmDialogDeletePlaylist(playlist: Playlist) {
        val messageText = getString(R.string.want_to_delete_a_playlist) +
                " \"" + playlist.playlistName +
                "\"?"
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(messageText)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deletePlaylist()
                findNavController().navigateUp()
            }.show()
    }

    private fun showToastNotTracksForShare() {
        Toast.makeText(requireContext(),
            R.string.not_tracks_for_share,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun sharePlaylist() {
        menuBottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        if (viewModel.getPlaylistFromDb().playlistTrackCount == 0L) {
            showToastNotTracksForShare()
        } else {
            viewModel.sharePlaylist()
        }
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