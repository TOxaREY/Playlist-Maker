package xyz.toxarey.playlistmaker.media_library.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import xyz.toxarey.playlistmaker.media_library.domain.SaveCoverRepository
import xyz.toxarey.playlistmaker.utils.DIRECTORY_WITH_COVERS
import xyz.toxarey.playlistmaker.utils.QUALITY_COMPRESSION
import java.io.File
import java.io.FileOutputStream

class SaveCoverRepositoryImpl(private val context: Context): SaveCoverRepository {
    override fun saveCoverToPrivateStorage(
        uri: Uri,
        coverPath: String
    ) {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            DIRECTORY_WITH_COVERS
        )
        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val file = File(
            filePath,
            coverPath
        )

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(
                Bitmap.CompressFormat.JPEG,
                QUALITY_COMPRESSION,
                outputStream
            )
    }
}