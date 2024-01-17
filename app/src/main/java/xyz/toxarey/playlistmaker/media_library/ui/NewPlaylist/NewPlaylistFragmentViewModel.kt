package xyz.toxarey.playlistmaker.media_library.ui.NewPlaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.media_library.domain.Playlist
import xyz.toxarey.playlistmaker.media_library.domain.PlaylistInteractor
import xyz.toxarey.playlistmaker.media_library.domain.SaveCoverInteractor

class NewPlaylistFragmentViewModel(
    private val interactorPlaylist: PlaylistInteractor,
    private val interactorSaveCover: SaveCoverInteractor
): ViewModel() {
    private val newPlaylistStateLiveData = MutableLiveData<NewPlaylistScreenState>()

    init {
        setAllEmptyNewPlaylistStateLiveData()
    }

    fun getNewPlaylistStateLiveData(): LiveData<NewPlaylistScreenState> = newPlaylistStateLiveData


    fun setAllEmptyNewPlaylistStateLiveData() {
        newPlaylistStateLiveData.postValue(NewPlaylistScreenState.AllEmpty)
    }

    fun setFieldNameIsNotEmptyNewPlaylistStateLiveData() {
        newPlaylistStateLiveData.postValue(NewPlaylistScreenState.FieldNameIsNotEmpty)
    }

    fun setFieldNameIsEmptyNewPlaylistStateLiveData() {
        newPlaylistStateLiveData.postValue(NewPlaylistScreenState.FieldNameIsEmpty)
    }

    fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactorPlaylist.insertPlaylist(playlist)
        }
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