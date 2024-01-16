package xyz.toxarey.playlistmaker.media_library.ui.Playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.FragmentPlaylistsBinding
import xyz.toxarey.playlistmaker.media_library.domain.Playlist
import xyz.toxarey.playlistmaker.media_library.domain.PlaylistsState
import xyz.toxarey.playlistmaker.utils.SPAN_COUNT_PLAYLISTS

class PlaylistsFragment: Fragment() {
    private val viewModel: PlaylistsFragmentViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlists = ArrayList<Playlist>()
    private var playlistsAdapter: PlaylistsAdapter? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(
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
        binding.newPlaylistButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }

        viewModel.getPlaylistsStateLiveData().observe(viewLifecycleOwner) {
            playlistsState(it)
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
        viewModel.getPlaylistsTracks()
    }

    private fun initializationAdapter() {
        playlistsAdapter = PlaylistsAdapter(playlists) { playlist ->
            if (clickDebounce()) {

            }
        }

        binding.rvPlaylists.layoutManager = GridLayoutManager(
            requireContext(),
            SPAN_COUNT_PLAYLISTS
        )
        binding.rvPlaylists.adapter = playlistsAdapter
    }

    private fun playlistsState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> updatePlaylistsScreenState(
                PlaylistsScreenState.SUCCESS,
                state.playlists
            )
            is PlaylistsState.Empty -> updatePlaylistsScreenState(
                PlaylistsScreenState.NOTHING_FOUND,
                null
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatePlaylistsScreenState(
        state: PlaylistsScreenState,
        newPlaylists: List<Playlist>?
    ) {
        when (state) {
            PlaylistsScreenState.SUCCESS -> {
                nothingFoundMessage(false)
                playlists.clear()
                if (newPlaylists != null) {
                    playlists.addAll(newPlaylists)
                    playlists.reverse()
                }
                playlistsAdapter?.notifyDataSetChanged()
            }

            PlaylistsScreenState.NOTHING_FOUND -> {
                nothingFoundMessage(true)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun nothingFoundMessage(isVisible: Boolean) {
        if(isVisible) {
            playlists.clear()
            playlistsAdapter?.notifyDataSetChanged()
            binding.nothingFoundImage.visibility = View.VISIBLE
            binding.notCreatedPlaylistsText.visibility = View.VISIBLE
        } else {
            binding.nothingFoundImage.visibility = View.GONE
            binding.notCreatedPlaylistsText.visibility = View.GONE
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