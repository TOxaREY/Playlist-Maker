package xyz.toxarey.playlistmaker.creator

import android.content.Context
import xyz.toxarey.playlistmaker.app.App
import xyz.toxarey.playlistmaker.player.data.TrackMediaPlayerRepositoryImpl
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerRepository
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerInteractorImpl
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerInteractor
import xyz.toxarey.playlistmaker.search.data.RetrofitNetworkClient
import xyz.toxarey.playlistmaker.search.data.SearchTrackStorageImpl
import xyz.toxarey.playlistmaker.search.data.TracksRepositoryImpl
import xyz.toxarey.playlistmaker.search.domain.TracksInteractor
import xyz.toxarey.playlistmaker.search.domain.TracksInteractorImpl
import xyz.toxarey.playlistmaker.search.domain.TracksRepository
import xyz.toxarey.playlistmaker.settings.data.SettingsRepositoryImpl
import xyz.toxarey.playlistmaker.settings.domain.SettingsInteractor
import xyz.toxarey.playlistmaker.settings.domain.SettingsInteractorImpl
import xyz.toxarey.playlistmaker.settings.domain.SettingsRepository
import xyz.toxarey.playlistmaker.sharing.data.ExternalNavigatorImpl
import xyz.toxarey.playlistmaker.sharing.domain.ExternalNavigator
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractor
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractorImpl
import xyz.toxarey.playlistmaker.utils.PLAYLISTMAKER_PREFERENCES

object Creator {
    fun provideTrackMediaPlayerInteractor(previewUrl: String): TrackMediaPlayerInteractor {
        return TrackMediaPlayerInteractorImpl(provideTrackMediaPlayer(previewUrl))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator(context))
    }

    fun provideSettingsInteractor(app: App): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(app))
    }

    fun provideSearchInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(provideTracksRepository(context))
    }

    private fun provideTrackMediaPlayer(previewUrl: String): TrackMediaPlayerRepository {
        return TrackMediaPlayerRepositoryImpl(previewUrl)
    }

    private fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    private fun provideSettingsRepository(app: App): SettingsRepository {
        return SettingsRepositoryImpl(app)
    }

    private fun provideTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(context),
            SearchTrackStorageImpl(context.getSharedPreferences(
                PLAYLISTMAKER_PREFERENCES,
                Context.MODE_PRIVATE)
            )
        )
    }
}