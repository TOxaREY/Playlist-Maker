package xyz.toxarey.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.player.ui.AudioPlayerViewModel
import xyz.toxarey.playlistmaker.search.ui.SearchViewModel
import xyz.toxarey.playlistmaker.settings.ui.SettingsActivityViewModel

val viewModelKoinModule = module {
    viewModel { (track: Track) ->
        AudioPlayerViewModel(
            track,
            get()
        )
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsActivityViewModel(
            get(),
            get()
        )
    }
}