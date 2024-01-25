package xyz.toxarey.playlistmaker.media_library.ui.PlaylistInfo

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInfo
import xyz.toxarey.playlistmaker.media_library.domain.Playlists.PlaylistInteractor
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractor
import xyz.toxarey.playlistmaker.utils.DIRECTORY_WITH_COVERS
import xyz.toxarey.playlistmaker.utils.countToString
import xyz.toxarey.playlistmaker.utils.durationOfAllTracksString
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoFragmentViewModel(
    private val playlistId: Long,
    private val interactorPlaylist: PlaylistInteractor,
    private val interactorSharing: SharingInteractor
): ViewModel() {
    private val playlistInfoLiveData = MutableLiveData<PlaylistInfo>()
    private var playlistInfo = PlaylistInfo()
    private var playlistFromDb = Playlist()
    val getPlaylistInfo = playlistInfoLiveData

    fun getPlaylist() {
        viewModelScope.launch {
            interactorPlaylist
                .getPlaylist(playlistId)
                .collect { playlist ->
                    playlistFromDb = playlist
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
                    playlistInfo.playlistTrackCount = playlist.playlistTrackCount
                    playlistInfo.playlistTrackCountString = countToString(playlist.playlistTrackCount)
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

    fun getPlaylistFromDb(): Playlist {
        return playlistFromDb
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            interactorPlaylist.deletePlaylist(playlistFromDb)
        }
    }

    private fun getPlaylistInfoString(): String {
        val builder = StringBuilder()
        val playlistTitle = "${playlistInfo.playlistName}\n${playlistInfo.playlistDescription ?:""}\n${playlistInfo.playlistTrackCountString}\n"
        builder.append(playlistTitle)
        playlistInfo.playlistTracks!!.forEachIndexed { index, it ->
            builder.append("${index + 1}. ${it.artistName} - ${it.trackName} (${
                SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            )
                .format(it.trackTimeMillis)})\n")
        }
        return builder.toString()
    }

    fun sharePlaylist() {
        viewModelScope.launch {
            interactorSharing.sharePlaylist(getPlaylistInfoString())
        }
    }
}