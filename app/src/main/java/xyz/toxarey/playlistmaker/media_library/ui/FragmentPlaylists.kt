package xyz.toxarey.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.databinding.FragmentPlaylistsBinding

class FragmentPlaylists: Fragment() {
    private val fragmentPlaylistsViewModel: FragmentPlaylistsViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    companion object {
        fun newInstance(): FragmentPlaylists {
            val args = Bundle()

            val fragment = FragmentPlaylists()
            fragment.arguments = args
            return fragment
        }
    }
}