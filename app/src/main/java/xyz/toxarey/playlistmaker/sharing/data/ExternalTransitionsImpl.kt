package xyz.toxarey.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.sharing.domain.ExternalTransitions

class ExternalTransitionsImpl(private val context: Context): ExternalTransitions {
    override fun shareAppAddress(address: String) {
        val shareIntent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                address
            )
        }, context.getString(R.string.share_app))
        context.startActivity(shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun writeToSupportMail(mail: String) {
        val supportIntent = Intent.createChooser(Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:" + mail)
            putExtra(
                Intent.EXTRA_SUBJECT,
                context.getString(R.string.theme_email)
            )
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.text_email)
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }, context.getString(R.string.write_to_support))
        context.startActivity(supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun termsOfUseAddress(address: String) {
        val termsOfUseIntent = Intent.createChooser(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(address)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }, context.getString(R.string.terms_of_use))
        context.startActivity(termsOfUseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun sharePlaylist(playlist: String) {
        val sharePlaylistIntent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                playlist
            )
        }, context.getString(R.string.share_playlist))
        context.startActivity(sharePlaylistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}