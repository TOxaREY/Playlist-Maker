package xyz.toxarey.playlistmaker.media_library.domain.SaveCover

import android.net.Uri

interface SaveCoverRepository {
    fun saveCoverToPrivateStorage(
        uri: Uri,
        coverPath: String
    )
}