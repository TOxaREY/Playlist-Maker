package xyz.toxarey.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.domain.SearchState
import xyz.toxarey.playlistmaker.search.domain.TracksInteractor

class SearchFragmentViewModel(private val tracksInteractor: TracksInteractor): ViewModel() {
    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> = _searchState
    fun setPauseSearchStateLiveData() {
        _searchState.postValue(SearchState.Paused)
    }

    fun addTrackToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun getTracksFromHistory(): ArrayList<Track> {
        return tracksInteractor.getTracksFromHistory()
    }

    fun removeTracksHistory() {
        return tracksInteractor.removeTracksHistory()
    }

    fun searchTrack(text: String) {
        if (text.isNotEmpty()) {
            _searchState.postValue(SearchState.Loading)
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(text)
                    .collect { queryResult ->
                        val searchTracks = arrayListOf<Track>()
                        if (queryResult.tracks != null && _searchState.value != SearchState.Paused) {
                            searchTracks.addAll(queryResult.tracks)
                        }

                        when {
                            queryResult.isError == true -> _searchState.postValue(SearchState.Error)
                            searchTracks.isEmpty() -> {
                                if (_searchState.value != SearchState.Paused) {
                                    _searchState.postValue(SearchState.Empty)
                                }
                            }
                            searchTracks.isNotEmpty() -> _searchState.postValue(SearchState.Content(searchTracks))
                        }
                    }
            }
        }
    }
}