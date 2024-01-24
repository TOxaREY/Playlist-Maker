package xyz.toxarey.playlistmaker.media_library.ui.PlaylistInfo

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInfo
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInteractor
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.utils.DIRECTORY_WITH_COVERS
import xyz.toxarey.playlistmaker.utils.countToString
import xyz.toxarey.playlistmaker.utils.durationOfAllTracksString
import java.io.File

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
                        playlistInfo.playlistTracks = listOf()
                        playlistInfo.playlistDurationOfAllTracks = durationOfAllTracksString(listOf())
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

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            interactorPlaylist
                .deleteTrackFromPlaylist(
                    playlistId,
                    track
                )
                .collect { isDeleted ->
                    if (isDeleted) {
                        getPlaylist()
                    }
                }
        }
    }

    fun getPlaylistCoverUri(context: Context): Uri? {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            DIRECTORY_WITH_COVERS
        )
        val file = playlistInfo.playlistCoverPath?.let {
            File(
                filePath,
                it
            )
        }
        return file?.toUri()
    }
}