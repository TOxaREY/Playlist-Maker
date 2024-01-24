package xyz.toxarey.playlistmaker.media_library.ui.PlaylistInfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
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
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInfo
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.utils.EXTRA_TRACK
import xyz.toxarey.playlistmaker.utils.PLAYLIST_ID

class PlaylistInfoFragment: Fragment() {
    private val viewModel: PlaylistInfoFragmentViewModel by viewModel {
        parametersOf(requireArguments().getLong(PLAYLIST_ID))
    }
    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var tracksBottomSheetAdapter: TracksPlaylistInfoAdapter? = null
    private var listTrack = ArrayList<Track>()
    private var isClickAllowed = true

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
        binding.backToMediaLibraryButtonFromPlaylistInfo.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getPlaylistInfo.observe(viewLifecycleOwner) { playlistInfo ->
            updatePlaylistInfo(playlistInfo)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
        bottomSheetBehavior.peekHeight =
            resources.displayMetrics.heightPixels -
                    resources.displayMetrics.widthPixels -
                    resources.getDimensionPixelSize(R.dimen.sum_height_items_playlist_info)

        viewModel.getPlaylist()
        initializationAdapter()
    }

    override fun onPause() {
        super.onPause()
        isClickAllowed = true
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
        binding.tvPlaylistInfoName.text = playlistInfo.playlistName
        binding.tvPlaylistInfoDescription.text = playlistInfo.playlistDescription
        binding.tvPlaylistInfoTime.text = playlistInfo.playlistDurationOfAllTracks
        binding.tvPlaylistInfoCountTracks.text = playlistInfo.playlistTrackCount
        listTrack.clear()
        listTrack.addAll(ArrayList(playlistInfo.playlistTracks!!))
        listTrack.reverse()
        tracksBottomSheetAdapter?.notifyDataSetChanged()
    }

    private fun initializationAdapter() {
        tracksBottomSheetAdapter = TracksPlaylistInfoAdapter(listTrack) { track, isLong ->
            if (clickDebounce()) {
                if (isLong) {
                    showConfirmDialog(track)
                } else {
                    segueToAudioPlayerFragment(track)
                }
            }
        }
        binding.rvListTrackBottomSheet.adapter = tracksBottomSheetAdapter
    }

    private fun segueToAudioPlayerFragment(track: Track) {
        findNavController().navigate(
            R.id.action_playlistInfoFragment_to_audioPlayerFragment,
            bundleOf(EXTRA_TRACK to track)
        )
    }

    private fun showConfirmDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.want_to_delete_a_track)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track)
            }.show()
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