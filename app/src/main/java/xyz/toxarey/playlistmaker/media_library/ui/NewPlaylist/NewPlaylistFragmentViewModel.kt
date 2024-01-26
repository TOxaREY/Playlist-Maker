package xyz.toxarey.playlistmaker.media_library.ui.NewPlaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInteractor
import xyz.toxarey.playlistmaker.media_library.domain.SaveCover.SaveCoverInteractor

open class NewPlaylistFragmentViewModel(
    private val interactorPlaylist: PlaylistInteractor,
    private val interactorSaveCover: SaveCoverInteractor
): ViewModel() {
    private val newPlaylistStateLiveData = MutableLiveData<NewPlaylistScreenState>()

    init {
        setAllEmptyNewPlaylistStateLiveData()
    }

    open fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactorPlaylist.insertPlaylist(playlist)
        }
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