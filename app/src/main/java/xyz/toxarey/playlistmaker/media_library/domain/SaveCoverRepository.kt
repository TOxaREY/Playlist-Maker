package xyz.toxarey.playlistmaker.media_library.domain

import android.net.Uri

interface SaveCoverRepository {
    fun saveCoverToPrivateStorage(
        uri: Uri,
        coverPath: String
    )
}