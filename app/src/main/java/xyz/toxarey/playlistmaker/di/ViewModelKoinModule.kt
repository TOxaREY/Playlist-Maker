package xyz.toxarey.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import xyz.toxarey.playlistmaker.media_library.ui.FavoriteTracksFragmentViewModel
import xyz.toxarey.playlistmaker.media_library.ui.PlaylistsFragmentViewModel
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.player.ui.AudioPlayerFragmentViewModel
import xyz.toxarey.playlistmaker.search.ui.SearchFragmentViewModel
import xyz.toxarey.playlistmaker.settings.ui.SettingsFragmentViewModel

val viewModelKoinModule = module {
    viewModel { (track: Track) ->
        AudioPlayerFragmentViewModel(
            track,
            get()
        )
    }

    viewModel {
        SearchFragmentViewModel(get())
    }

    viewModel {
        SettingsFragmentViewModel(
            get(),
            get()
        )
    }

    viewModel {
        PlaylistsFragmentViewModel()
    }

    viewModel {
        FavoriteTracksFragmentViewModel()
    }
}