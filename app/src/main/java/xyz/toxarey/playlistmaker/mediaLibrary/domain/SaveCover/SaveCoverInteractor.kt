package xyz.toxarey.playlistmaker.mediaLibrary.domain.SaveCover

import android.net.Uri

interface SaveCoverInteractor {
    fun saveCoverToPrivateStorage(
        uri: Uri,
        coverPath: String
    )
}