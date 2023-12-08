package xyz.toxarey.playlistmaker.search.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.domain.Result

class RetrofitNetworkClient(
    private val iTunesSearchService: ItunesSearchAPI,
    private val context: Context
): NetworkClient {
    override suspend fun requestTracks(request: TracksRequest): Result<List<Track>> {
        if (!isConnected()) {
            return Result.Error(true)
        }

        return withContext(Dispatchers.IO) {
            try {
                val results = iTunesSearchService.search(request.text).results
                Result.Success(results)
            } catch (e: Throwable) {
                Result.Error(true)
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}