package xyz.toxarey.playlistmaker.media_library.ui.PlaylistInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInfo
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInteractor
import xyz.toxarey.playlistmaker.utils.countToString
import xyz.toxarey.playlistmaker.utils.durationOfAllTracksString

class PlaylistInfoFragmentViewModel(
    private val playlistId: Long,
    private val interactorPlaylist: PlaylistInteractor
): ViewModel() {
    private val playlistInfoLiveData = MutableLiveData<PlaylistInfo>()
    private var playlistInfo = PlaylistInfo()
    val getPlaylistInfo = playlistInfoLiveData

    fun getPlaylist() {
        viewModelScope.launch {
            interactorPlaylist
                .getPlaylist(playlistId)
                .collect { playlist ->
                    if (playlist.playlistTrackIdList != null) {
                        interactorPlaylist
                            .getListTrackFromPlaylist(playlist.playlistTrackIdList!!)
                            .collect { listTrack ->
                                playlistInfo.playlistTracks = listTrack
                                playlistInfo.playlistDurationOfAllTracks = durationOfAllTracksString(listTrack)
                            }
                    } else {
                        playlistInfo.playlistDurationOfAllTracks = durationOfAllTracksString(ArrayList())
                    }
                    playlistInfo.playlistId = playlist.playlistId
                    playlistInfo.playlistName = playlist.playlistName
                    playlistInfo.playlistDescription = playlist.playlistDescription
                    playlistInfo.playlistCoverPath = playlist.playlistCoverPath
                    playlistInfo.playlistTrackCount = countToString(playlist.playlistTrackCount)
                    playlistInfoLiveData.postValue(playlistInfo)
                }
        }
    }
}