package xyz.toxarey.playlistmaker.di

import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.toxarey.playlistmaker.search.data.ItunesSearchAPI
import xyz.toxarey.playlistmaker.search.data.NetworkClient
import xyz.toxarey.playlistmaker.search.data.RetrofitNetworkClient
import xyz.toxarey.playlistmaker.search.data.SearchTrackStorage
import xyz.toxarey.playlistmaker.search.data.SearchTrackStorageImpl

val dataKoinModule = module {
    val iTunesSearchBaseUrl = "http://itunes.apple.com"
    single<ItunesSearchAPI> {
        Retrofit.Builder()
            .baseUrl(iTunesSearchBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesSearchAPI::class.java)
    }

    single { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(
            get(),
            get()
        )
    }

    single<SearchTrackStorage> {
        SearchTrackStorageImpl(
            get(),
            get()
        )
    }
}