package xyz.toxarey.playlistmaker.utils

import xyz.toxarey.playlistmaker.player.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

fun durationOfAllTracksString(listTrack: List<Track>): String {
    var durationSum = 0L
    listTrack.forEach {
        durationSum += it.trackTimeMillis
    }
    val durationSumString = SimpleDateFormat(
        "mm",
        Locale.getDefault()
    ).format(durationSum)

    return when {
        durationSumString.toLong() in 5..20 -> "$durationSumString минут"
        durationSumString.toString().endsWith('1') -> "$durationSumString минута"
        durationSumString.toString().endsWith('2') -> "$durationSumString минуты"
        durationSumString.toString().endsWith('3') -> "$durationSumString минуты"
        durationSumString.toString().endsWith('4') -> "$durationSumString минуты"
        else -> "$durationSumString минут"
    }
}