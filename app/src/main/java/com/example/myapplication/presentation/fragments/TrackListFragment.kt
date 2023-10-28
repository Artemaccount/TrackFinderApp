package com.example.myapplication.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class TrackListFragment : Fragment() {

    val trackViewModel: TrackViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    @Inject
    lateinit var router: Router

    private val binding by lazy { TrackListFragmentBinding.inflate(layoutInflater) }


    private val adapter by lazy {
        TracksAdapter(getAppComponent(), object : OnInteractionListener {
            override fun select(track: Track) {
                router.navigateTo(Screens.selectedTrack(track.trackId))
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
                binding.trackRecyclerView.visibility = View.GONE
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
}


fun Fragment.getAppComponent(): AppComponent =
    (activity?.applicationContext as TrackFinderApplication).appComponent