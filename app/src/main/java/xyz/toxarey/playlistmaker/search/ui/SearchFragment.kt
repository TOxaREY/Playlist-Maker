package xyz.toxarey.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.R
import xyz.toxarey.playlistmaker.utils.EXTRA_TRACK
import xyz.toxarey.playlistmaker.databinding.FragmentSearchBinding
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.search.domain.SearchState

class SearchFragment: Fragment() {
    private val viewModel: SearchFragmentViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var tracksAdapter: TracksAdapter? = null
    private var tracksAdapterHistory: TracksAdapter? = null
    private val tracks = ArrayList<Track>()
    private var tracksHistory = ArrayList<Track>()
    private var searchText = ""
    private var isClickAllowed = true
    private var searchJob: Job? = null
    private var isSegueToAudioPlayerFragment = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(
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
                requestTrackDebounce(inputMethodManager)
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
            if (isSegueToAudioPlayerFragment) {
                isSegueToAudioPlayerFragment = false
            } else {
                clearSearchText()
            }
        }
        isClickAllowed = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializationAdapters() {
        tracksAdapter = TracksAdapter(tracks) { track ->
            if (clickDebounce()) {
                viewModel.addTrackToHistory(track)
                tracksHistory.clear()
                tracksHistory.addAll(viewModel.getTracksFromHistory())
                tracksAdapterHistory?.notifyDataSetChanged()
                segueToAudioPlayerFragment(track)
            }
        }

        tracksAdapterHistory = TracksAdapter(tracksHistory) { track ->
            if (clickDebounce()) {
                segueToAudioPlayerFragment(track)
            }
        }

        binding.rvTracks.adapter = tracksAdapter
        binding.rvSearchHistory.adapter = tracksAdapterHistory
    }

    private fun segueToAudioPlayerFragment(track: Track) {
        isSegueToAudioPlayerFragment = true
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
            viewModel.setPauseSearchStateLiveData()
            clearSearchText()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun addHistoryTracks(tracks: ArrayList<Track>) {
        tracksHistory.clear()
        tracksHistory.addAll(tracks)
        tracksAdapterHistory?.notifyDataSetChanged()
    }

    private fun clearSearchButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun requestTrackDebounce(inputMethodManager: InputMethodManager?) {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            requestTrack(inputMethodManager)
        }
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
                updateSearchScreenState(
                    SearchScreenState.PAUSED,
                    null
                )
                searchJob?.cancel()
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

            SearchScreenState.PAUSED -> {
                binding.progressBar.visibility = View.GONE
                communicationErrorMessage(false)
                nothingFoundMessage(false)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun nothingFoundMessage(isVisible: Boolean) {
        if(isVisible) {
            binding.progressBar.visibility = View.GONE
            tracksAdapter?.notifyDataSetChanged()
            binding.nothingFoundImage.visibility = View.VISIBLE
            binding.nothingFoundText.visibility = View.VISIBLE
        } else {
            binding.nothingFoundImage.visibility = View.GONE
            binding.nothingFoundText.visibility = View.GONE
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun clearTrackList() {
        tracks.clear()
        tracksAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTracksToList(newTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        tracksAdapter?.notifyDataSetChanged()
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

    private fun clearSearchText() {
        binding.searchEditText.setText("")
        searchText = ""
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}