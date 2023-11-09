package xyz.toxarey.playlistmaker.di

import org.koin.dsl.module
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerInteractor
import xyz.toxarey.playlistmaker.player.domain.TrackMediaPlayerInteractorImpl
import xyz.toxarey.playlistmaker.search.domain.TracksInteractor
import xyz.toxarey.playlistmaker.search.domain.TracksInteractorImpl
import xyz.toxarey.playlistmaker.settings.domain.SettingsInteractor
import xyz.toxarey.playlistmaker.settings.domain.SettingsInteractorImpl
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractor
import xyz.toxarey.playlistmaker.sharing.domain.SharingInteractorImpl

val interactorKoinModule = module {
    factory<TrackMediaPlayerInteractor> {
        TrackMediaPlayerInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}