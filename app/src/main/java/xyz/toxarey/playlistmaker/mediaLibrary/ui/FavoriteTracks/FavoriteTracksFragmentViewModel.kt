package xyz.toxarey.playlistmaker.mediaLibrary.ui.FavoriteTracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.mediaLibrary.domain.FavoriteTracks.FavoriteTracksInteractor
import xyz.toxarey.playlistmaker.mediaLibrary.domain.FavoriteTracks.FavoriteTracksState

class FavoriteTracksFragmentViewModel(private val interactorFavoriteTracks: FavoriteTracksInteractor): ViewModel() {
    private val _favoriteTracksState = MutableLiveData<FavoriteTracksState>()
    init {
        setFavoriteTracksState()
    }

    val favoriteTracksState: LiveData<FavoriteTracksState> = _favoriteTracksState

    fun setFavoriteTracksState() {
        viewModelScope.launch {
            interactorFavoriteTracks
                .getFavoriteTracks()
                .collect { favoriteTracks ->
                    if (favoriteTracks.isEmpty()) {
                        _favoriteTracksState.postValue(FavoriteTracksState.Empty)
                    } else {
                        _favoriteTracksState.postValue(FavoriteTracksState.Content(favoriteTracks))
                    }
                }
        }
    }
}