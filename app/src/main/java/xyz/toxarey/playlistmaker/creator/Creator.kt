package xyz.toxarey.playlistmaker.creator

import android.content.Context
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
import xyz.toxarey.playlistmaker.sharing.data.ExternalTransitionsImpl
import xyz.toxarey.playlistmaker.sharing.domain.ExternalTransitions
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractor
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractorImpl
import xyz.toxarey.playlistmaker.utils.PLAYLISTMAKER_PREFERENCES

object Creator {

    fun provideTrackMediaPlayerInteractor(previewUrl: String) : TrackMediaPlayerInteractor {
        return TrackMediaPlayerInteractorImpl(provideTrackMediaPlayer(previewUrl))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator(context))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(context))
    }

    fun provideSearchInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(provideTracksRepository(context))
    }

   fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    private fun provideTrackMediaPlayer(previewUrl: String) : TrackMediaPlayerRepository {
        return TrackMediaPlayerRepositoryImpl(previewUrl)
    }

    private fun provideExternalNavigator(context: Context): ExternalTransitions {
        return ExternalTransitionsImpl(context)
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