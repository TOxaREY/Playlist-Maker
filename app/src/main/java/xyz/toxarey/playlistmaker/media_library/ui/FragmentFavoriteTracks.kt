package xyz.toxarey.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.databinding.FragmentFavoriteTracksBinding

class FragmentFavoriteTracks: Fragment() {
    private val fragmentFavoriteTracksViewModel: FragmentFavoriteTracksViewModel by viewModel()
    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    companion object {
        fun newInstance(): FragmentFavoriteTracks {
            val args = Bundle()

            val fragment = FragmentFavoriteTracks()
            fragment.arguments = args
            return fragment
        }
    }
}