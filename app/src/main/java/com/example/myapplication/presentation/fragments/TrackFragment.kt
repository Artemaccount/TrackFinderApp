package com.example.myapplication.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.presentation.adapter.TracksAdapter
import com.example.myapplication.application.TrackFinderApplication
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.di.TrackViewModelFactory
import com.example.myapplication.utils.KeyboardUtils.hideKeyboard
import com.example.myapplication.presentation.viewmodel.TrackViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackFragment : Fragment() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var vmFactory: TrackViewModelFactory

    private lateinit var trackViewModel: TrackViewModel

    private var firstTimeAction = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity?.applicationContext as TrackFinderApplication).appComponent.inject(this)
        trackViewModel = ViewModelProvider(this, vmFactory).get(TrackViewModel::class.java)

        binding = ActivityMainBinding.inflate(inflater, container, false)

        val adapter = TracksAdapter()
        binding.trackRecyclerView.adapter = adapter

        binding.searchButton.setOnClickListener {
            val searchText = binding.searchTrackEditText.text.toString()
            if (searchText.isBlank()) {
                binding.searchTrackEditText.error = getString(R.string.enter_value_error)
            } else {
                trackViewModel.getTracksNew(searchText)
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                trackViewModel.data.onEach { results ->
                    val trackList = results.results
                    if (trackList.isNotEmpty()) {
                        showNoValueFound(false)
                        Log.d("MyTagBro", "trackList is not empty: $trackList")
                        adapter.submitList(trackList)
                        activity?.currentFocus?.hideKeyboard()
                    } else {
                        if (firstTimeAction) firstTimeAction = false else showNoValueFound(true)
                        Log.d("MyTagBro", "trackList is empty")
                    }
                }.collect()
            }
        }
        return binding.root
    }

    private fun showNoValueFound(value: Boolean) {
        if (value) {
            binding.noValueFoundTextView.visibility = View.VISIBLE
            binding.trackRecyclerView.visibility = View.GONE
        } else {
            binding.noValueFoundTextView.visibility = View.GONE
            binding.trackRecyclerView.visibility = View.VISIBLE
        }
    }
}