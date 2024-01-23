package xyz.toxarey.playlistmaker.media_library.ui.PlaylistInfo

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.FragmentPlaylistInfoBinding
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInfo
import xyz.toxarey.playlistmaker.utils.DIRECTORY_WITH_COVERS
import xyz.toxarey.playlistmaker.utils.PLAYLIST_ID
import java.io.File

class PlaylistInfoFragment: Fragment() {
    private val viewModel: PlaylistInfoFragmentViewModel by viewModel {
        parametersOf(requireArguments().getLong(PLAYLIST_ID))
    }
    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!

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

        viewModel.getPlaylistInfo.observe(viewLifecycleOwner) {
            updatePlaylistInfo(it)
        }
        viewModel.getPlaylist()
    }

    private fun updatePlaylistInfo(playlistInfo: PlaylistInfo) {
        if (!playlistInfo.playlistCoverPath.isNullOrEmpty()) {
            val filePath = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                DIRECTORY_WITH_COVERS
            )
            val file = playlistInfo.playlistCoverPath?.let {
                File(
                    filePath,
                    it
                )
            }
            val uri = file?.toUri()
            binding.ivPlaylistCoverFull.setPadding(
                0,
                0,
                0, 
                0
            )
            Glide.with(this)
                .load(uri)
                .centerCrop()
                .placeholder(R.drawable.album_placeholder_full)
                .into(binding.ivPlaylistCoverFull)
        }
        binding.tvPlaylistInfoName.text = playlistInfo.playlistName
        binding.tvPlaylistInfoDescription.text = playlistInfo.playlistDescription
        binding.tvPlaylistInfoTime.text = playlistInfo.playlistDurationOfAllTracks
        binding.tvPlaylistInfoCountTracks.text = playlistInfo.playlistTrackCount
    }
}