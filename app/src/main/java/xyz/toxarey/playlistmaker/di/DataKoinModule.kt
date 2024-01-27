package xyz.toxarey.playlistmaker.di

import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.toxarey.playlistmaker.mediaLibrary.data.AppDatabase
import xyz.toxarey.playlistmaker.mediaLibrary.data.Playlists.PlaylistDbConvertor
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

    single {
        Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "database.db"
            ).build()
    }

    factory {
        PlaylistDbConvertor(get())
    }
}