package xyz.toxarey.playlistmaker.sharing.domain

import xyz.toxarey.playlistmaker.utils.MY_EMAIL
import xyz.toxarey.playlistmaker.utils.SHARE_ADDRESS
import xyz.toxarey.playlistmaker.utils.TERMS_OF_USE_ADDRESS

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator): SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareAppAddress(SHARE_ADDRESS)
    }

    override fun writeToSupport() {
        externalNavigator.writeToSupportMail(MY_EMAIL)
    }

    override fun termsOfUse() {
        externalNavigator.termsOfUseAddress(TERMS_OF_USE_ADDRESS)
    }
}