package xyz.toxarey.playlistmaker.media_library.domain.SaveCover

import android.net.Uri

interface SaveCoverInteractor {
    fun saveCoverToPrivateStorage(
        uri: Uri,
        coverPath: String
    )
}