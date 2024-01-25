package xyz.toxarey.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import xyz.toxarey.playlistmaker.media_library.ui.FavoriteTracks.FavoriteTracksFragmentViewModel
import xyz.toxarey.playlistmaker.media_library.ui.NewPlaylist.NewPlaylistFragmentViewModel
import xyz.toxarey.playlistmaker.media_library.ui.PlaylistInfo.PlaylistInfoFragmentViewModel
import xyz.toxarey.playlistmaker.media_library.ui.Playlists.PlaylistsFragmentViewModel
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.player.ui.AudioPlayerFragmentViewModel
import xyz.toxarey.playlistmaker.search.ui.SearchFragmentViewModel
import xyz.toxarey.playlistmaker.settings.ui.SettingsFragmentViewModel

val viewModelKoinModule = module {
    viewModel { (track: Track) ->
        AudioPlayerFragmentViewModel(
            track,
            get(),
            get(),
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
        PlaylistsFragmentViewModel(get())
    }

    viewModel {
        FavoriteTracksFragmentViewModel(get())
    }

    viewModel {
        NewPlaylistFragmentViewModel(
            get(),
            get()
        )
    }

    viewModel { (playlistId: Long) ->
        PlaylistInfoFragmentViewModel(
            playlistId,
            get(),
            get()
        )
    }
}