package xyz.toxarey.playlistmaker.mediaLibrary.ui.Playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.PlaylistInteractor
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.PlaylistsState

class PlaylistsFragmentViewModel(private val interactorPlaylist: PlaylistInteractor): ViewModel() {
    private val _playlistsState = MutableLiveData<PlaylistsState>()
    init {
        getPlaylistsTracks()
    }
    val playlistsState: LiveData<PlaylistsState> = _playlistsState

    fun getPlaylistsTracks() {
        viewModelScope.launch {
            interactorPlaylist
                .getPlaylists()
                .collect { playlists ->
                    if (playlists.isEmpty()) {
                        _playlistsState.postValue(PlaylistsState.Empty)
                    } else {
                        _playlistsState.postValue(PlaylistsState.Content(playlists))
                    }
                }
        }
    }
}