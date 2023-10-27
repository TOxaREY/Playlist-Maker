package xyz.toxarey.playlistmaker.search.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.toxarey.playlistmaker.search.domain.TracksResponse

interface ItunesSearchAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>
}