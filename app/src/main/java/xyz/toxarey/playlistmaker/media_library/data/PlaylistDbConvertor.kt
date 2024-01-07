package xyz.toxarey.playlistmaker.media_library.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import xyz.toxarey.playlistmaker.media_library.domain.Playlist

class PlaylistDbConvertor(private val gson: Gson) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistCoverPath,
            convertPlaylistIdTrackListToString(playlist.playlistTrackIdList),
            playlist.playlistTrackCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistCoverPath,
            convertStringToPlaylistTrackList(playlist.playlistTrackIdList),
            playlist.playlistTrackCount
        )
    }

    private fun convertPlaylistIdTrackListToString(playlistTrackIdList: ArrayList<Long>?): String? {
        return if (playlistTrackIdList.isNullOrEmpty()) {
            null
        } else {
            gson.toJson(playlistTrackIdList)
        }
    }

    private fun convertStringToPlaylistTrackList(playlistTrackIdList: String?): ArrayList<Long>? {
        return if (playlistTrackIdList.isNullOrEmpty()) {
            null
        } else {
            val arrayType = object : TypeToken<ArrayList<Long>>() {}.type
            gson.fromJson(
                playlistTrackIdList,
                arrayType
            )
        }
    }
}