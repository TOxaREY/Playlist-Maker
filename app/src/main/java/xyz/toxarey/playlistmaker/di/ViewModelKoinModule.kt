package xyz.toxarey.playlistmaker.di

import android.content.Intent
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import xyz.toxarey.playlistmaker.player.ui.AudioPlayerViewModel
import xyz.toxarey.playlistmaker.search.ui.SearchViewModel
import xyz.toxarey.playlistmaker.settings.ui.SettingsActivityViewModel

val viewModelKoinModule = module {
    viewModel { (intent: Intent) ->
        AudioPlayerViewModel(
            intent,
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