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
    private val searchStateLiveData = MutableLiveData<SearchState>()
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData
    fun setPauseSearchStateLiveData() {
        searchStateLiveData.postValue(SearchState.Paused)
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
            searchStateLiveData.postValue(SearchState.Loading)
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(text)
                    .collect { pair ->
                        val searchTracks = arrayListOf<Track>()
                        if (pair.first != null) {
                            searchTracks.addAll(pair.first!!)
                        }

                        when {
                            pair.second == true -> searchStateLiveData.postValue(SearchState.Error)
                            searchTracks.isEmpty() -> searchStateLiveData.postValue(SearchState.Empty)
                            searchTracks.isNotEmpty() -> searchStateLiveData.postValue(SearchState.Content(searchTracks))
                        }
                    }
            }
        }
    }
}