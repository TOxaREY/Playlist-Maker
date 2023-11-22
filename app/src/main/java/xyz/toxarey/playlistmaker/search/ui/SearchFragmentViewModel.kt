package xyz.toxarey.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.domain.SearchState
import xyz.toxarey.playlistmaker.search.domain.TracksInteractor

class SearchFragmentViewModel(private val tracksInteractor: TracksInteractor): ViewModel() {
    private val searchStateLiveData = MutableLiveData<SearchState>()
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData
    fun setDefaultSearchStateLiveData() {
        searchStateLiveData.postValue(SearchState.Default)
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
            tracksInteractor.searchTracks(text, object: TracksInteractor.TracksConsumer {
                override fun consume(
                    tracks: List<Track>?,
                    isError: Boolean?
                ) {
                    val searchTracks = arrayListOf<Track>()
                    if (tracks != null) {
                        searchTracks.addAll(tracks)
                    }

                    when {
                        isError == true -> searchStateLiveData.postValue(SearchState.Error)
                        searchTracks.isEmpty() -> searchStateLiveData.postValue(SearchState.Empty)
                        searchTracks.isNotEmpty() -> searchStateLiveData.postValue(SearchState.Content(searchTracks))
                    }
                }
            } )
        }
    }
}