package xyz.toxarey.playlistmaker.search.ui

import xyz.toxarey.playlistmaker.player.domain.Track

fun interface CellClickListener {
    fun onCellClickListener(track: Track)
}