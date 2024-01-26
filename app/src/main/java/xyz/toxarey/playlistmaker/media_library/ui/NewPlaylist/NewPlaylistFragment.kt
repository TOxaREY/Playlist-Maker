package xyz.toxarey.playlistmaker.media_library.ui.NewPlaylist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.databinding.FragmentNewPlaylistBinding
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.Playlist

open class NewPlaylistFragment: Fragment() {
    open val viewModel: NewPlaylistFragmentViewModel by viewModel()
    private var _binding: FragmentNewPlaylistBinding? = null
    val binding get() = _binding!!
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    open var newPlaylist = Playlist()
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(
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
        viewModel.getNewPlaylistStateLiveData().observe(viewLifecycleOwner) {
            newPlaylistState(it)
        }

        binding.backToMediaLibraryButtonFromNewPlaylist.setOnClickListener {
            onBackToMediaLibrary()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackToMediaLibrary()
            }
        })

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(requireActivity())
                    .load(uri)
                    .centerCrop()
                    .transform(
                        CenterCrop(),
                        RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.track_album_full_imageView_roundedCorners))
                    )
                    .placeholder(R.drawable.album_placeholder_full)
                    .error(R.drawable.album_placeholder_full)
                    .into(binding.ivNewPlaylist)

                saveCover(uri)
            }
        }

        binding.ivNewPlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_complete_creation_playlist_title)
            .setMessage(R.string.confirm_complete_creation_playlist_message)
            .setNeutralButton(R.string.title_button_cancel) { _, _ -> }
            .setPositiveButton(R.string.title_button_complete) { _, _ ->
                findNavController().navigateUp()
            }

        binding.tiNamePlaylist.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                newPlaylist.playlistName = text.toString()
            } else {
                newPlaylist.playlistName = null
            }
            setNewPlaylistStateLiveData()
        }

        binding.tiDescriptionPlaylist.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                newPlaylist.playlistDescription = text.toString()
            } else {
                newPlaylist.playlistDescription = null
            }
            setNewPlaylistStateLiveData()
        }

        binding.createPlaylistButton.setOnClickListener {
            if (clickDebounce()) {
                viewModel.savePlaylist(newPlaylist)
                showToastSavePlaylist()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun onBackToMediaLibrary() {
        if (viewModel.getNewPlaylistStateLiveData().value == NewPlaylistScreenState.AllEmpty) {
            findNavController().navigateUp()
        } else {
            confirmDialog.show()
        }
    }

    open fun saveCover(uri: Uri) {
        val coverPath = (System.currentTimeMillis() / 1000).toString() + ".jpg"
        newPlaylist.playlistCoverPath = coverPath
        setNewPlaylistStateLiveData()
        viewModel.saveCover(
            uri,
            coverPath
        )
    }

    open fun showToastSavePlaylist() {
        val toastText = getString(R.string.toast_creation_text_playlist) +
                " " + newPlaylist.playlistName +
                " " + getString(R.string.toast_creation_text_created)
        Toast.makeText(requireContext(),
            toastText,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun newPlaylistState(state: NewPlaylistScreenState) {
        when (state) {
            is NewPlaylistScreenState.AllEmpty -> isEnabledCreatePlaylistButton(false)
            is NewPlaylistScreenState.FieldNameIsEmpty -> isEnabledCreatePlaylistButton(false)
            is NewPlaylistScreenState.FieldNameIsNotEmpty -> isEnabledCreatePlaylistButton(true)
        }
    }

    private fun isEnabledCreatePlaylistButton(isEnabled: Boolean) {
        if (isEnabled) {
            binding.createPlaylistButton.isEnabled = true
            binding.createPlaylistButton.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(),
                R.color.yp_blue
            )
        } else {
            binding.createPlaylistButton.isEnabled = false
            binding.createPlaylistButton.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(),
                R.color.yp_text_gray
            )
        }
    }

    private fun setNewPlaylistStateLiveData() {
        if (newPlaylist.playlistName.isNullOrEmpty()
            && newPlaylist.playlistDescription.isNullOrEmpty()
            && newPlaylist.playlistCoverPath.isNullOrEmpty()
            ) {
            viewModel.setAllEmptyNewPlaylistStateLiveData()
        } else if (newPlaylist.playlistName.isNullOrEmpty()) {
            viewModel.setFieldNameIsEmptyNewPlaylistStateLiveData()
        } else {
            viewModel.setFieldNameIsNotEmptyNewPlaylistStateLiveData()
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