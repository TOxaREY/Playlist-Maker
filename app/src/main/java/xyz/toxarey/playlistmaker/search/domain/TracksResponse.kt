package xyz.toxarey.playlistmaker.search.domain

import xyz.toxarey.playlistmaker.player.domain.Track

class TracksResponse(val results: List<Track>): Result<List<Track>>()