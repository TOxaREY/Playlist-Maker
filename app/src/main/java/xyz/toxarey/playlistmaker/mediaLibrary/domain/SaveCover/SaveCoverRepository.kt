package xyz.toxarey.playlistmaker.mediaLibrary.domain.SaveCover

import android.net.Uri

interface SaveCoverRepository {
    fun saveCoverToPrivateStorage(
        uri: Uri,
        coverPath: String
    )
}