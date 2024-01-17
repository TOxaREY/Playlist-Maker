package xyz.toxarey.playlistmaker.media_library.domain

import android.net.Uri

class SaveCoverInteractorImpl(private val saveCoverRepository: SaveCoverRepository): SaveCoverInteractor {
    override fun saveCoverToPrivateStorage(
        uri: Uri,
        coverPath: String
    ) {
        return saveCoverRepository.saveCoverToPrivateStorage(
            uri,
            coverPath
        )
    }
}