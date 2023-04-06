package xyz.toxarey.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val iTunesSearchBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesSearchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesSearchService = retrofit.create(ItunesSearchAPI::class.java)
    private val tracks = ArrayList<Track>()
    private val tracksHistory = ArrayList<Track>()
    private var searchText = ""
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var tracksAdapterHistory: TracksAdapter
    private lateinit var searchEditText: EditText
    private lateinit var nothingFoundImage: ImageView
    private lateinit var nothingFoundText: TextView
    private lateinit var communicationErrorImage: ImageView
    private lateinit var communicationErrorText: TextView
    private lateinit var updateButton: Button
    private lateinit var searchHistoryLinear: LinearLayout
    private lateinit var clearHistoryButton: Button
    private lateinit var backToMainButton: Button
    private lateinit var clearSearchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchHistory = SearchHistory(getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE))
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        initializationAdapters(searchHistory)
        initializationViews()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                nothingFoundMessage(false)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChangedAction(s)
            }

            override fun afterTextChanged(s: Editable?) {
                afterTextChangedAction(searchHistory.read())
            }
        }
        buttonsListeners(inputMethodManager, searchHistory)
        searchEditTextListeners(simpleTextWatcher, searchHistory.read())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoreText = savedInstanceState.getString(SEARCH_TEXT, "")
        searchEditText.setText(restoreText)
        searchEditText.setSelection(searchEditText.text.length)
        searchText = restoreText
    }

    private fun clearSearchButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    private fun nothingFoundMessage(isVisible: Boolean) {
        if(isVisible) {
            tracksAdapter.notifyDataSetChanged()
            nothingFoundImage.visibility = View.VISIBLE
            nothingFoundText.visibility = View.VISIBLE
        } else {
            nothingFoundImage.visibility = View.GONE
            nothingFoundText.visibility = View.GONE
        }
    }

    private fun communicationErrorMessage(isVisible: Boolean) {
        if(isVisible) {
            clearTrackList()
            communicationErrorImage.visibility = View.VISIBLE
            communicationErrorText.visibility = View.VISIBLE
            updateButton.visibility = View.VISIBLE
        } else {
            communicationErrorImage.visibility = View.GONE
            communicationErrorText.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
    }

    private fun requestTrack() {
        if (searchEditText.text.isNotEmpty()) {
            iTunesSearchService.search(searchEditText.text.toString()).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            tracksAdapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            nothingFoundMessage(true)
                        }
                    } else {
                        communicationErrorMessage(true)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    communicationErrorMessage(true)
                }
            })
        }
    }

    private fun clearTrackList() {
        tracks.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun addHistoryTracks(tracks: ArrayList<Track>) {
        tracksHistory.clear()
        tracksHistory.addAll(tracks)
        tracksAdapterHistory.notifyDataSetChanged()
    }

    private fun searchEditTextListeners(simpleTextWatcher: TextWatcher, tracks: ArrayList<Track>) {
        searchEditText.addTextChangedListener(simpleTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                requestTrack()
                true
            }
            false
        }
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            addHistoryTracks(tracks)
            searchHistoryLinear.visibility = if (hasFocus && searchEditText.text.isEmpty() && tracks.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun buttonsListeners(inputMethodManager: InputMethodManager?, searchHistory: SearchHistory) {
        backToMainButton.setOnClickListener {
            finish()
        }
        clearSearchButton.setOnClickListener {
            searchEditText.setText("")
            searchText = ""
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            clearTrackList()
        }
        updateButton.setOnClickListener {
            communicationErrorMessage(false)
            requestTrack()
        }
        clearHistoryButton.setOnClickListener {
            searchHistory.remove()
            searchHistoryLinear.visibility = View.GONE
        }
    }

    private fun initializationAdapters(searchHistory: SearchHistory) {
        val rvTracks = findViewById<RecyclerView>(R.id.rvTracks)
        val rvSearchHistory = findViewById<RecyclerView>(R.id.rvSearchHistory)
        tracksAdapter = TracksAdapter(tracks, searchHistory)
        tracksAdapterHistory = TracksAdapter(tracksHistory, null)
        rvTracks.adapter = tracksAdapter
        rvSearchHistory.adapter = tracksAdapterHistory
    }

    private fun initializationViews() {
        nothingFoundImage = findViewById(R.id.nothingFoundImage)
        nothingFoundText = findViewById(R.id.nothingFoundText)
        communicationErrorImage = findViewById(R.id.communicationErrorImage)
        communicationErrorText = findViewById(R.id.communicationErrorText)
        updateButton = findViewById(R.id.update_button)
        searchHistoryLinear = findViewById(R.id.search_history_linear)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        backToMainButton = findViewById(R.id.back_to_main_button_from_search)
        searchEditText = findViewById(R.id.search_edit_text)
        clearSearchButton = findViewById(R.id.clear_search_button)
    }

    private fun onTextChangedAction(s: CharSequence?) {
        clearSearchButton.visibility = clearSearchButtonVisibility(s)
        searchHistoryLinear.visibility = if (searchEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
    }

    private fun afterTextChangedAction(tracks: ArrayList<Track>) {
        searchText = searchEditText.text.toString()
        if (searchText.isEmpty()) {
            clearTrackList()
            addHistoryTracks(tracks)
            searchHistoryLinear.visibility = if (tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}