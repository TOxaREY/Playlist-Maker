package xyz.toxarey.playlistmaker.mediaLibrary.ui.NewPlaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.PlaylistInteractor
import xyz.toxarey.playlistmaker.mediaLibrary.domain.SaveCover.SaveCoverInteractor

open class NewPlaylistFragmentViewModel(
    private val interactorPlaylist: PlaylistInteractor,
    private val interactorSaveCover: SaveCoverInteractor
): ViewModel() {
    private val _newPlaylistState = MutableLiveData<NewPlaylistScreenState>()

    init {
        setAllEmptyNewPlaylistState()
    }

    open fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactorPlaylist.insertPlaylist(playlist)
        }
    }

    val newPlaylistState: LiveData<NewPlaylistScreenState> = _newPlaylistState


    fun setAllEmptyNewPlaylistState() {
        _newPlaylistState.postValue(NewPlaylistScreenState.AllEmpty)
    }

    fun setFieldNameIsNotEmptyNewPlaylistState() {
        _newPlaylistState.postValue(NewPlaylistScreenState.FieldNameIsNotEmpty)
    }

    fun setFieldNameIsEmptyNewPlaylistState() {
        _newPlaylistState.postValue(NewPlaylistScreenState.FieldNameIsEmpty)
    }

    fun saveCover(
        uri: Uri,
        coverPath: String
    ) {
        interactorSaveCover.saveCoverToPrivateStorage(
            uri,
            coverPath
        )
    }
}