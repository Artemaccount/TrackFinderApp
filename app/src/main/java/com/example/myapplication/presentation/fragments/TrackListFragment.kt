package com.example.myapplication.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.example.myapplication.application.TrackFinderApplication
import com.example.myapplication.data.api.model.Loading
import com.example.myapplication.data.api.model.NoDataFound
import com.example.myapplication.data.api.model.Result
import com.example.myapplication.data.api.model.Track
import com.example.myapplication.databinding.TrackListFragmentBinding
import com.example.myapplication.di.AppComponent
import com.example.myapplication.presentation.intents.TrackListIntent
import com.example.myapplication.presentation.adapter.OnInteractionListener
import com.example.myapplication.presentation.adapter.TracksAdapter
import com.example.myapplication.presentation.viewmodel.TrackListViewModel
import com.example.myapplication.utils.KeyboardUtils.hideKeyboard
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackListFragment : Fragment() {

    private val trackListViewModel: TrackListViewModel by viewModels {
        getAppComponent().viewModelFactory()
    }

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var router: Router

    private val binding by lazy { TrackListFragmentBinding.inflate(layoutInflater) }

    private val adapter by lazy {
        TracksAdapter(glide, object : OnInteractionListener {
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
        bindIntents()
        observeViewModel()
        return binding.root
    }

    private fun bindIntents() {
        binding.searchTrackEditText.addTextChangedListener {
            it?.let {
                lifecycleScope.launch {
                    val intent = TrackListIntent.GetTracksByRequest(it.toString()) {
                        adapter.submitList(emptyList())
                    }
                    trackListViewModel.userIntent.send(intent)
                }
            }
        }
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            trackListViewModel.state.collect {
                with(binding) {
                    when (it) {
                        Loading -> {
                            noValueFoundTextView.visibility = View.GONE
                            progressBar.visibility = View.VISIBLE
                            trackRecyclerView.visibility = View.GONE
                        }

                        NoDataFound -> {
                            progressBar.visibility = View.GONE
                            noValueFoundTextView.visibility = View.VISIBLE
                            trackRecyclerView.visibility = View.GONE
                        }

                        is Result.Success -> {
                            adapter.submitList(it.data.results)
                            noValueFoundTextView.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            trackRecyclerView.visibility = View.VISIBLE
                            activity?.currentFocus?.hideKeyboard()
                        }

                        is Result.Error -> {
                            activity?.currentFocus?.hideKeyboard()
                            progressBar.visibility = View.GONE
                            trackRecyclerView.visibility = View.GONE
                            Snackbar.make(
                                binding.noValueFoundTextView,
                                "Something went wrong...Please, try again",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

fun Fragment.getAppComponent(): AppComponent =
    (activity?.applicationContext as TrackFinderApplication).appComponent