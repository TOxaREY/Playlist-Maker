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
    private val tracksAdapter = TracksAdapter(tracks)
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
    lateinit var searchEditText: EditText
    lateinit var notingFoundImage: ImageView
    lateinit var notingFoundText: TextView
    lateinit var communicationErrorImage: ImageView
    lateinit var communicationErrorText: TextView
    lateinit var updateButton: Button
    var searchText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        notingFoundImage = findViewById(R.id.notingFoundImage)
        notingFoundText = findViewById(R.id.notingFoundText)
        communicationErrorImage = findViewById(R.id.communicationErrorImage)
        communicationErrorText = findViewById(R.id.communicationErrorText)
        updateButton = findViewById(R.id.update_button)
        val rvTracks = findViewById<RecyclerView>(R.id.rvTracks)
        rvTracks.adapter = tracksAdapter

        val backToMainButton = findViewById<Button>(R.id.back_to_main_button_from_search)
        searchEditText = findViewById(R.id.search_edit_text)
        val clearSearchButton = findViewById<Button>(R.id.clear_search_button)
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

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

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                nothingFoundMessage(false)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearSearchButton.visibility = clearSearchButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = searchEditText.text.toString()
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                requestTrack()
                true
            }
            false
        }
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
            notingFoundImage.visibility = View.VISIBLE
            notingFoundText.visibility = View.VISIBLE
        } else {
            notingFoundImage.visibility = View.GONE
            notingFoundText.visibility = View.GONE
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
}