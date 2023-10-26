package xyz.toxarey.playlistmaker.sharing.domain

interface ExternalTransitions {
    fun shareAppAddress(address: String)
    fun writeToSupportMail(mail: String)
    fun termsOfUseAddress(address: String)
}