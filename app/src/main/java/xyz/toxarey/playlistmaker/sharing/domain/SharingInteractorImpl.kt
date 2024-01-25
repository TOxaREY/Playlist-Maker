package xyz.toxarey.playlistmaker.sharing.domain

import xyz.toxarey.playlistmaker.utils.MY_EMAIL
import xyz.toxarey.playlistmaker.utils.SHARE_ADDRESS
import xyz.toxarey.playlistmaker.utils.TERMS_OF_USE_ADDRESS

class SharingInteractorImpl(private val externalTransitions: ExternalTransitions): SharingInteractor {
    override fun shareApp() {
        externalTransitions.shareAppAddress(SHARE_ADDRESS)
    }

    override fun writeToSupport() {
        externalTransitions.writeToSupportMail(MY_EMAIL)
    }

    override fun termsOfUse() {
        externalTransitions.termsOfUseAddress(TERMS_OF_USE_ADDRESS)
    }

    override fun sharePlaylist(playlist: String) {
        externalTransitions.sharePlaylist(playlist)
    }
}