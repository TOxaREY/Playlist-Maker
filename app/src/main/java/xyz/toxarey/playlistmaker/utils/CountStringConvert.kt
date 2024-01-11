package xyz.toxarey.playlistmaker.utils

fun countToString(countTracks: Long): String {
    return when {
        countTracks in 5..20 -> "$countTracks треков"
        countTracks.toString().endsWith('1') -> "$countTracks трек"
        countTracks.toString().endsWith('2') -> "$countTracks трека"
        countTracks.toString().endsWith('3') -> "$countTracks трека"
        countTracks.toString().endsWith('4') -> "$countTracks трека"
        else -> "$countTracks треков"
    }
}