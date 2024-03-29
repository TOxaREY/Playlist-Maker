package xyz.toxarey.playlistmaker.mediaLibrary.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import xyz.toxarey.playlistmaker.mediaLibrary.ui.FavoriteTracks.FavoriteTracksFragment
import xyz.toxarey.playlistmaker.mediaLibrary.ui.Playlists.PlaylistsFragment
import xyz.toxarey.playlistmaker.utils.NUMBER_OF_PAGES_IN_MEDIALIBRARY_FRAGMENT

class MediaLibraryPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return NUMBER_OF_PAGES_IN_MEDIALIBRARY_FRAGMENT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteTracksFragment()
            else -> PlaylistsFragment()
        }
    }
}