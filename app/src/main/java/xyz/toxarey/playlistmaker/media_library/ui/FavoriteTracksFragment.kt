package xyz.toxarey.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.databinding.FragmentFavoriteTracksBinding

class FavoriteTracksFragment: Fragment() {
    private val favoriteTracksFragmentViewModel: FavoriteTracksFragmentViewModel by viewModel()
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): FavoriteTracksFragment {
            val args = Bundle()

            val fragment = FavoriteTracksFragment()
            fragment.arguments = args
            return fragment
        }
    }
}