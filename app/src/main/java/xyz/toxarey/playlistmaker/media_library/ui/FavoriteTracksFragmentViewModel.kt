package xyz.toxarey.playlistmaker.media_library.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.media_library.domain.FavoriteTracksInteractor
import xyz.toxarey.playlistmaker.media_library.domain.FavoriteTracksState
import xyz.toxarey.playlistmaker.player.domain.Track

class FavoriteTracksFragmentViewModel(private val interactorFavoriteTracks: FavoriteTracksInteractor): ViewModel() {
    private val favoriteTracksStateLiveData = MutableLiveData<FavoriteTracksState>()
    init {
        getFavoriteTracks()
    }

    fun getFavoriteTracksStateLiveData(): LiveData<FavoriteTracksState> = favoriteTracksStateLiveData

    fun getFavoriteTracks() {
        viewModelScope.launch {
            interactorFavoriteTracks
                .getFavoriteTracks()
                .collect { favoriteTracks ->
                    if (favoriteTracks.isEmpty()) {
                        favoriteTracksStateLiveData.postValue(FavoriteTracksState.Empty)
                    } else {
                        favoriteTracksStateLiveData.postValue(FavoriteTracksState.Content(favoriteTracks))
                    }
                }
        }
    }
}