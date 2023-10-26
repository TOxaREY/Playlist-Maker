package xyz.toxarey.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import xyz.toxarey.playlistmaker.utils.EXTRA_TRACK
import xyz.toxarey.playlistmaker.databinding.ActivitySearchBinding
import xyz.toxarey.playlistmaker.player.domain.Track
import xyz.toxarey.playlistmaker.player.ui.AudioPlayerActivity
import xyz.toxarey.playlistmaker.search.domain.SearchScreenState
import xyz.toxarey.playlistmaker.search.domain.SearchState

class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var tracksAdapterHistory: TracksAdapter
    private lateinit var handler: Handler
    private lateinit var requestTrackRunnable: Runnable
    private val tracks = ArrayList<Track>()
    private var tracksHistory = ArrayList<Track>()
    private var searchText = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        viewModel.getSearchStateLiveData().observe(this) {
            searchState(it)
        }

        initializationAdapters()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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

        buttonsListeners(inputMethodManager)
        searchEditTextListeners(
            simpleTextWatcher,
            viewModel.getTracksFromHistory()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            SEARCH_TEXT,
            searchText
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoreText = savedInstanceState.getString(
            SEARCH_TEXT,
            ""
        )
        binding.searchEditText.setText(restoreText)
        binding.searchEditText.setSelection(binding.searchEditText.text.length)
        searchText = restoreText
    }

    private fun initializationAdapters() {
        val onClickListenerTracks = CellClickListener { track ->
            viewModel.addTrackToHistory(track)
            tracksHistory.clear()
            tracksHistory.addAll(viewModel.getTracksFromHistory())
            tracksAdapterHistory.notifyDataSetChanged()
            segueToAudioPlayerActivity(track)
        }

        val onClickListenerTracksHistory = CellClickListener { track ->
            segueToAudioPlayerActivity(track)
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

    private fun segueToAudioPlayerActivity(track: Track) {
        val audioPlayerIntent = Intent(
            this,
            AudioPlayerActivity::class.java
        )
        audioPlayerIntent.putExtra(
            EXTRA_TRACK,
            track
        )
        startActivity(audioPlayerIntent)
    }

    private fun buttonsListeners(inputMethodManager: InputMethodManager?) {
        binding.backToMainButtonFromSearch.setOnClickListener {
            finish()
        }

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

    private fun searchEditTextListeners(
        simpleTextWatcher: TextWatcher,
        tracks: ArrayList<Track>
    ) {
        binding.searchEditText.addTextChangedListener(simpleTextWatcher)
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
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
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}