package xyz.toxarey.playlistmaker.sharing.domain

interface ExternalNavigator {
    fun shareAppAddress(address: String)
    fun writeToSupportMail(mail: String)
    fun termsOfUseAddress(address: String)
}