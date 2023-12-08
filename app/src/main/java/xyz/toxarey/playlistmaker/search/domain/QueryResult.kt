package xyz.toxarey.playlistmaker.search.domain

import xyz.toxarey.playlistmaker.player.domain.Track

data class QueryResult(val tracks: List<Track>?, val isError: Boolean?)
