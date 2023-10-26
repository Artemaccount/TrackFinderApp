package com.example.myapplication.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.application.TrackFinderApplication
import com.example.myapplication.data.api.DataState
import com.example.myapplication.data.api.Loading
import com.example.myapplication.data.api.Result
import com.example.myapplication.data.api.model.Results
import com.example.myapplication.data.api.model.Track
import com.example.myapplication.databinding.TrackListFragmentBinding
import com.example.myapplication.di.AppComponent
import com.example.myapplication.presentation.adapter.OnInteractionListener
import com.example.myapplication.presentation.adapter.TracksAdapter
import com.example.myapplication.presentation.viewmodel.TrackViewModel
import com.example.myapplication.utils.KeyboardUtils.hideKeyboard
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TrackListFragment : Fragment() {

    val trackViewModel: TrackViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    private val binding by lazy { TrackListFragmentBinding.inflate(layoutInflater) }

    private val adapter by lazy {
        TracksAdapter(getAppComponent(), object : OnInteractionListener {
            override fun select(track: Track) {
                val selectedTrackFragment = SelectedTrackFragment()
                selectedTrackFragment.arguments = bundleOf(TRACK_ID_KEY to track.trackId)

                activity?.let {
                    it.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedTrackFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.getAppComponent().inject(this)

        binding.trackRecyclerView.adapter = adapter

        binding.searchTrackEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    trackViewModel.input.value = it.toString()
                }
            }
        })

        trackViewModel.data.onEach { dataState ->
            activity?.currentFocus?.hideKeyboard()
            showDataByState(dataState)
        }.launchIn(lifecycleScope)

        return binding.root
    }

    private fun showDataByState(dataState: DataState<Results>) {
        with(binding) {
            when (dataState) {
                Loading -> {
                    noValueFoundTextView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    trackRecyclerView.visibility = View.GONE
                }

                is Result.Success -> {
                    adapter.submitList(dataState.data.results)
                    noValueFoundTextView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    trackRecyclerView.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    trackRecyclerView.visibility = View.GONE
                    noValueFoundTextView.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        const val TRACK_ID_KEY = "track_id"
    }
}


fun Fragment.getAppComponent(): AppComponent =
    (activity?.applicationContext as TrackFinderApplication).appComponent