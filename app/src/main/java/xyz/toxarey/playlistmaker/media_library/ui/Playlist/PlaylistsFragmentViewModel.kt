package xyz.toxarey.playlistmaker.media_library.ui.Playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.media_library.domain.PlaylistInteractor
import xyz.toxarey.playlistmaker.media_library.domain.PlaylistsState

class PlaylistsFragmentViewModel(private val interactorPlaylist: PlaylistInteractor): ViewModel() {
    private val playlistsStateLiveData = MutableLiveData<PlaylistsState>()
    init {
        getPlaylistsTracks()
    }
    fun getPlaylistsStateLiveData(): LiveData<PlaylistsState> = playlistsStateLiveData

    fun getPlaylistsTracks() {
        viewModelScope.launch {
            interactorPlaylist
                .getPlaylists()
                .collect { playlists ->
                    if (playlists.isEmpty()) {
                        playlistsStateLiveData.postValue(PlaylistsState.Empty)
                    } else {
                        playlistsStateLiveData.postValue(PlaylistsState.Content(playlists))
                    }
                }
        }
    }
}