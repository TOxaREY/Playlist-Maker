package xyz.toxarey.playlistmaker.media_library.data

import xyz.toxarey.playlistmaker.player.domain.Track

class TrackDbConvertor {
    fun map(track: Track): TrackInFavoriteEntity {
        return TrackInFavoriteEntity(
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

    fun map(track: TrackInFavoriteEntity): Track {
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
}