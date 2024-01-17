package xyz.toxarey.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import xyz.toxarey.playlistmaker.media_library.data.FavoriteTracksRepositoryImpl
import xyz.toxarey.playlistmaker.media_library.data.PlaylistRepositoryImpl
import xyz.toxarey.playlistmaker.media_library.data.SaveCoverRepositoryImpl
import xyz.toxarey.playlistmaker.media_library.data.TrackDbConvertor
import xyz.toxarey.playlistmaker.media_library.domain.FavoriteTracksRepository
import xyz.toxarey.playlistmaker.media_library.domain.PlaylistRepository
import xyz.toxarey.playlistmaker.media_library.domain.SaveCoverRepository
import xyz.toxarey.playlistmaker.player.data.TrackMediaPlayerRepositoryImpl
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerRepository
import xyz.toxarey.playlistmaker.search.data.TracksRepositoryImpl
import xyz.toxarey.playlistmaker.search.domain.TracksRepository
import xyz.toxarey.playlistmaker.settings.data.SettingsRepositoryImpl
import xyz.toxarey.playlistmaker.settings.domain.SettingsRepository
import xyz.toxarey.playlistmaker.sharing.data.ExternalTransitionsImpl
import xyz.toxarey.playlistmaker.sharing.domain.ExternalTransitions
import xyz.toxarey.playlistmaker.utils.PLAYLISTMAKER_PREFERENCES

val repositoryKoinModule = module {
    factory { MediaPlayer() }

    factory<TrackMediaPlayerRepository> {
        TrackMediaPlayerRepositoryImpl(get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(
            get(),
            get()
        )
    }

    factory { TrackDbConvertor() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            get(),
            get()
        )
    }

    single {
        androidContext()
            .getSharedPreferences(
                PLAYLISTMAKER_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalTransitions> {
        ExternalTransitionsImpl(get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            get(),
            get()
        )
    }

    single<SaveCoverRepository> {
        SaveCoverRepositoryImpl(get())
    }
}