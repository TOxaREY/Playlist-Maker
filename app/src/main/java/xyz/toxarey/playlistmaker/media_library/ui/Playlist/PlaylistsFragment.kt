package xyz.toxarey.playlistmaker.media_library.ui.Playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.FragmentPlaylistsBinding

class PlaylistsFragment: Fragment() {
    private val playlistsFragmentViewModel: PlaylistsFragmentViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}