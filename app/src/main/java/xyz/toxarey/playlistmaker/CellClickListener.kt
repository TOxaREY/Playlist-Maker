package xyz.toxarey.playlistmaker

import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track

interface CellClickListener {
    fun onCellClickListener(track: Track)
}