package xyz.toxarey.playlistmaker.media_library.ui.EditingPlaylist

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import xyz.toxarey.playlistmaker.media_library.ui.NewPlaylist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.utils.EDITABLE_PLAYLIST

class EditingPlaylistFragment: NewPlaylistFragment() {
    override val viewModel: EditingPlaylistFragmentViewModel by viewModel {
        parametersOf(requireArguments().getSerializable(EDITABLE_PLAYLIST))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createPlaylistButton.text = getString(R.string.save)
        binding.tvTitleNewPlaylist.text = getString(R.string.edit)
        viewModel.getEditablePlaylist.observe(viewLifecycleOwner) {
            newPlaylist = it
            fillFieldsWithData(it)
        }
    }

    override fun onBackToMediaLibrary() {
        findNavController().navigateUp()
    }

    override fun showToastSavePlaylist() {}

    private fun fillFieldsWithData(editablePlaylist: Playlist) {
        binding.tiNamePlaylist.editText?.text = Editable.Factory.getInstance().newEditable(editablePlaylist.playlistName)
        binding.tiDescriptionPlaylist.editText?.text = Editable.Factory.getInstance().newEditable(editablePlaylist.playlistDescription ?: "")
        if (editablePlaylist.playlistCoverPath != null) {
            val uri = viewModel.getPlaylistCoverUri(
                requireContext(),
                editablePlaylist
            )
            Glide.with(this)
                .load(uri)
                .centerCrop()
                .transform(
                    CenterCrop(),
                    RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners))
                )
                .placeholder(R.drawable.album_placeholder_full)
                .error(R.drawable.album_placeholder_full)
                .into(binding.ivNewPlaylist)

            if (uri != null) {
                saveCover(uri)
            }
        }
    }
}