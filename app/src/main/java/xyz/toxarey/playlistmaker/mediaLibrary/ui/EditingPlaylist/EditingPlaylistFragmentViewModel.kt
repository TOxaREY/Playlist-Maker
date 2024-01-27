package xyz.toxarey.playlistmaker.mediaLibrary.ui.EditingPlaylist

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.PlaylistInteractor
import xyz.toxarey.playlistmaker.mediaLibrary.domain.SaveCover.SaveCoverInteractor
import xyz.toxarey.playlistmaker.mediaLibrary.ui.NewPlaylist.NewPlaylistFragmentViewModel
import xyz.toxarey.playlistmaker.utils.DIRECTORY_WITH_COVERS
import java.io.File

class EditingPlaylistFragmentViewModel(
    editablePlaylist: Playlist,
    private val interactorPlaylist: PlaylistInteractor,
    interactorSaveCover: SaveCoverInteractor
) : NewPlaylistFragmentViewModel(
    interactorPlaylist, interactorSaveCover
) {
    private val _editablePlaylist = MutableLiveData<Playlist>()
    val editablePlaylist: LiveData<Playlist> = _editablePlaylist

    init {
        _editablePlaylist.postValue(editablePlaylist)
    }

    override fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactorPlaylist.updatePlaylist(playlist)
        }
    }

    fun getPlaylistCoverUri(
        context: Context,
        editablePlaylist: Playlist
    ): Uri? {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            DIRECTORY_WITH_COVERS
        )
        val file = editablePlaylist.playlistCoverPath?.let {
            File(
                filePath,
                it
            )
        }
        return file?.toUri()
    }
}