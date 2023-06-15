package xyz.toxarey.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import xyz.toxarey.playlistmaker.audioplayer.domain.models.Track

class SearchHistory(var sharedPrefs: SharedPreferences) {
    fun read(): ArrayList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: return arrayListOf()
        val arrayType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, arrayType)
    }

    fun add(track: Track) {
        val arrayListTracks = read()
        if (arrayListTracks.isEmpty()) {
            arrayListTracks.add(0, track)
        } else {
            val index = indexCopyTrack(arrayListTracks, track)
            if (index != null) {
                arrayListTracks.removeAt(index)
            }
            if (arrayListTracks.size == 10) {
                arrayListTracks.removeAt(9)
            }
            arrayListTracks.add(0, track)
        }
        write(arrayListTracks)
    }

    fun remove() {
        sharedPrefs.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    private fun write(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    private fun indexCopyTrack(tracks: ArrayList<Track>, track: Track): Int? {
        val index = tracks.indexOfFirst { it.trackId == track.trackId }
        return if (index == -1) {
            null
        } else {
            index
        }
    }
}