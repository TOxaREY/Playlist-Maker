package xyz.toxarey.playlistmaker.mediaLibrary.data.Playlists

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import xyz.toxarey.playlistmaker.mediaLibrary.domain.Playlists.Playlist
import xyz.toxarey.playlistmaker.player.domain.Track

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

    fun map(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: TrackInPlaylistEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
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