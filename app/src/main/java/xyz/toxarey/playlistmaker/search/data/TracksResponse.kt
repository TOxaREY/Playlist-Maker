package xyz.toxarey.playlistmaker.search.data

import xyz.toxarey.playlistmaker.player.domain.Track

class TracksResponse(val results: List<Track>): Response()