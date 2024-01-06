package xyz.toxarey.playlistmaker.media_library.ui.FavoriteTracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.FragmentFavoriteTracksBinding
import xyz.toxarey.playlistmaker.media_library.domain.FavoriteTracksState
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.ui.TracksAdapter
import xyz.toxarey.playlistmaker.utils.EXTRA_TRACK

class FavoriteTracksFragment: Fragment() {
    private val viewModel: FavoriteTracksFragmentViewModel by viewModel()
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val tracks = ArrayList<Track>()
    private var tracksAdapter: TracksAdapter? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(
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
        viewModel.getFavoriteTracksStateLiveData().observe(viewLifecycleOwner) {
            favoriteTracksState(it)
        }

        initializationAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        isClickAllowed = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTracks()
    }

    private fun initializationAdapter() {
        tracksAdapter = TracksAdapter(
            tracks
        ) { track ->
            if (clickDebounce()) {
                segueToAudioPlayerFragment(track)
            }
        }

        binding.rvFavoriteTracks.adapter = tracksAdapter
    }

    private fun segueToAudioPlayerFragment(track: Track) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
            bundleOf(EXTRA_TRACK to track)
        )
    }

    private fun favoriteTracksState(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> updateFavoriteTracksScreenState(
                FavoriteTracksScreenState.SUCCESS,
                state.tracks
            )
            is FavoriteTracksState.Empty -> updateFavoriteTracksScreenState(
                FavoriteTracksScreenState.NOTHING_FOUND,
                null
            )
        }
    }

    private fun updateFavoriteTracksScreenState(
        state: FavoriteTracksScreenState,
        newTracks: List<Track>?
    ) {
        when (state) {
            FavoriteTracksScreenState.SUCCESS -> {
                nothingFoundMessage(false)
                tracks.clear()
                if (newTracks != null) {
                    tracks.addAll(newTracks)
                    tracks.reverse()
                }
                tracksAdapter?.notifyDataSetChanged()
            }

            FavoriteTracksScreenState.NOTHING_FOUND -> {
                nothingFoundMessage(true)
            }
        }
    }

    private fun nothingFoundMessage(isVisible: Boolean) {
        if(isVisible) {
            tracks.clear()
            tracksAdapter?.notifyDataSetChanged()
            binding.nothingFoundImage.visibility = View.VISIBLE
            binding.mediaLibraryIsEmptyText.visibility = View.VISIBLE
        } else {
            binding.nothingFoundImage.visibility = View.GONE
            binding.mediaLibraryIsEmptyText.visibility = View.GONE
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