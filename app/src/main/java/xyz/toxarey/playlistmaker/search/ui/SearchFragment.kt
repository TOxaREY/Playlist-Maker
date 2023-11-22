package xyz.toxarey.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.utils.EXTRA_TRACK
import xyz.toxarey.playlistmaker.databinding.FragmentSearchBinding
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.domain.SearchScreenState
import xyz.toxarey.playlistmaker.search.domain.SearchState

class SearchFragment: Fragment() {
    private val viewModel: SearchFragmentViewModel by viewModel()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var tracksAdapterHistory: TracksAdapter
    private lateinit var handler: Handler
    private lateinit var requestTrackRunnable: Runnable
    private val tracks = ArrayList<Track>()
    private var tracksHistory = ArrayList<Track>()
    private var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        viewModel.getSearchStateLiveData().observe(viewLifecycleOwner) {
            searchState(it)
        }

        initializationAdapters()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        handler = Handler(Looper.getMainLooper())
        requestTrackRunnable = Runnable { requestTrack(inputMethodManager) }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                nothingFoundMessage(false)
                communicationErrorMessage(false)
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                onTextChangedAction(s)
                requestTrackDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                afterTextChangedAction(viewModel.getTracksFromHistory())
            }
        }

        searchEditTextListeners(simpleTextWatcher)
        buttonsListeners(inputMethodManager)
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.getSearchStateLiveData().value != SearchState.Content(tracks)) {
            viewModel.setPauseSearchStateLiveData()
            searchState(SearchState.Paused)
        }
    }

    private fun initializationAdapters() {
        val onClickListenerTracks = CellClickListener { track ->
            viewModel.addTrackToHistory(track)
            tracksHistory.clear()
            tracksHistory.addAll(viewModel.getTracksFromHistory())
            tracksAdapterHistory.notifyDataSetChanged()
            segueToAudioPlayerFragment(track)
        }

        val onClickListenerTracksHistory = CellClickListener { track ->
            segueToAudioPlayerFragment(track)
        }

        tracksAdapter = TracksAdapter(
            tracks,
            onClickListenerTracks
        )

        tracksAdapterHistory = TracksAdapter(
            tracksHistory,
            onClickListenerTracksHistory
        )

        binding.rvTracks.adapter = tracksAdapter
        binding.rvSearchHistory.adapter = tracksAdapterHistory
    }

    private fun segueToAudioPlayerFragment(track: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_audioPlayerFragment,
            bundleOf(EXTRA_TRACK to track)
        )
    }

    private fun buttonsListeners(inputMethodManager: InputMethodManager?) {
        binding.updateButton.setOnClickListener {
            communicationErrorMessage(false)
            requestTrack(inputMethodManager)
        }

        binding.clearSearchButton.setOnClickListener {
            binding.searchEditText.setText("")
            searchText = ""
            inputMethodManager?.hideSoftInputFromWindow(
                binding.searchEditText.windowToken,
                0
            )
            clearTrackList()
        }

        binding.clearHistoryButton.setOnClickListener {
            tracksHistory.clear()
            viewModel.removeTracksHistory()
            binding.searchHistoryLinear.visibility = View.GONE
        }
    }

    private fun searchEditTextListeners(simpleTextWatcher: TextWatcher) {
        binding.searchEditText.addTextChangedListener(simpleTextWatcher)
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            val tracks = viewModel.getTracksFromHistory()
            addHistoryTracks(tracks)
            binding.searchHistoryLinear.visibility = if (
                hasFocus && binding.searchEditText.text.isEmpty() && tracks.isNotEmpty()
            ) View.VISIBLE else View.GONE
        }
    }

    private fun onTextChangedAction(s: CharSequence?) {
        binding.clearSearchButton.visibility = clearSearchButtonVisibility(s)
        binding.searchHistoryLinear.visibility = if (
            binding.searchEditText.hasFocus() && s?.isEmpty() == true
        ) View.VISIBLE else View.GONE
    }

    private fun afterTextChangedAction(tracks: ArrayList<Track>) {
        searchText = binding.searchEditText.text.toString()
        if (searchText.isEmpty()) {
            clearTrackList()
            addHistoryTracks(tracks)
            binding.searchHistoryLinear.visibility = if (
                tracksHistory.isNotEmpty()
            ) View.VISIBLE else View.GONE
        }
    }

    private fun addHistoryTracks(tracks: ArrayList<Track>) {
        tracksHistory.clear()
        tracksHistory.addAll(tracks)
        tracksAdapterHistory.notifyDataSetChanged()
    }

    private fun clearSearchButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun requestTrackDebounce() {
        handler.removeCallbacks(requestTrackRunnable)
        handler.postDelayed(
            requestTrackRunnable,
            SEARCH_DEBOUNCE_DELAY
        )
    }

    private fun searchState(state: SearchState) {
        when (state) {
            is SearchState.Loading -> updateSearchScreenState(
                SearchScreenState.PROGRESS,
                null
            )
            is SearchState.Error -> updateSearchScreenState(
                SearchScreenState.COMMUNICATION_ERROR,
                null
            )
            is SearchState.Empty -> updateSearchScreenState(
                SearchScreenState.NOTHING_FOUND,
                null
            )
            is SearchState.Content -> updateSearchScreenState(
                SearchScreenState.SUCCESS,
                state.tracks
            )
            is SearchState.Paused -> {
                handler.removeCallbacks(requestTrackRunnable)
                binding.searchEditText.setText("")
                searchText = ""
            }
        }
    }

    private fun updateSearchScreenState(
        state: SearchScreenState,
        newTracks: List<Track>?
    ) {
        when (state) {
            SearchScreenState.PROGRESS -> {
                binding.progressBar.visibility = View.VISIBLE
                communicationErrorMessage(false)
                nothingFoundMessage(false)
            }

            SearchScreenState.COMMUNICATION_ERROR -> {
                communicationErrorMessage(true)
                nothingFoundMessage(false)
            }

            SearchScreenState.NOTHING_FOUND -> {
                communicationErrorMessage(false)
                nothingFoundMessage(true)
            }

            SearchScreenState.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                communicationErrorMessage(false)
                nothingFoundMessage(false)
                if (newTracks != null) {
                    addTracksToList(newTracks)
                }
            }
        }
    }

    private fun communicationErrorMessage(isVisible: Boolean) {
        if(isVisible) {
            binding.progressBar.visibility = View.GONE
            clearTrackList()
            binding.communicationErrorImage.visibility = View.VISIBLE
            binding.communicationErrorText.visibility = View.VISIBLE
            binding.updateButton.visibility = View.VISIBLE
        } else {
            binding.communicationErrorImage.visibility = View.GONE
            binding.communicationErrorText.visibility = View.GONE
            binding.updateButton.visibility = View.GONE
        }
    }

    private fun nothingFoundMessage(isVisible: Boolean) {
        if(isVisible) {
            binding.progressBar.visibility = View.GONE
            tracksAdapter.notifyDataSetChanged()
            binding.nothingFoundImage.visibility = View.VISIBLE
            binding.nothingFoundText.visibility = View.VISIBLE
        } else {
            binding.nothingFoundImage.visibility = View.GONE
            binding.nothingFoundText.visibility = View.GONE
        }
    }


    private fun clearTrackList() {
        tracks.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun addTracksToList(newTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        tracksAdapter.notifyDataSetChanged()
    }

    private fun requestTrack(inputMethodManager: InputMethodManager?) {
        if (binding.searchEditText.text.isNotEmpty()) {
            inputMethodManager?.hideSoftInputFromWindow(
                binding.searchEditText.windowToken,
                0
            )
            binding.progressBar.visibility = View.VISIBLE
            viewModel.searchTrack(searchText)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}