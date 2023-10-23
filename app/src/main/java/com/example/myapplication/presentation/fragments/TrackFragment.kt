package com.example.myapplication.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.application.TrackFinderApplication
import com.example.myapplication.data.api.State
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.di.AppComponent
import com.example.myapplication.presentation.adapter.TracksAdapter
import com.example.myapplication.presentation.viewmodel.TrackViewModel
import com.example.myapplication.utils.KeyboardUtils.hideKeyboard
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TrackFragment : Fragment() {
    private lateinit var binding: ActivityMainBinding

    private val adapter by lazy { TracksAdapter() }

    private val trackViewModel: TrackViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.getAppComponent().inject(this)

        binding = ActivityMainBinding.inflate(inflater, container, false)

        binding.trackRecyclerView.adapter = adapter

        binding.searchButton.setOnClickListener {
            val searchText = binding.searchTrackEditText.text.toString()
            if (searchText.isBlank()) {
                binding.searchTrackEditText.error = getString(R.string.enter_value_error)
            } else {
                trackViewModel.getTracks(searchText)
            }
        }

        trackViewModel.state.onEach {
            activity?.currentFocus?.hideKeyboard()
            with(binding) {
                when (it.state) {
                    State.Loading -> {
                        noValueFoundTextView.visibility = View.GONE
                        pBar.visibility = View.VISIBLE
                        trackRecyclerView.visibility = View.GONE
                    }

                    State.Success -> {
                        noValueFoundTextView.visibility = View.GONE
                        pBar.visibility = View.GONE
                        trackRecyclerView.visibility = View.VISIBLE
                    }

                    State.Error -> {
                        pBar.visibility = View.GONE
                        trackRecyclerView.visibility = View.GONE
                        noValueFoundTextView.visibility = View.VISIBLE
                    }
                }
            }
        }.launchIn(lifecycleScope)

        trackViewModel.data.onEach { results ->
            val trackList = results.results
            if (trackList.isNotEmpty()) {
                adapter.submitList(trackList)
            }
        }.launchIn(lifecycleScope)

        return binding.root
    }

    private fun Fragment.getAppComponent(): AppComponent =
        (activity?.applicationContext as TrackFinderApplication).appComponent

}