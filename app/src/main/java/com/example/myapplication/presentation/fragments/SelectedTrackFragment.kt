package com.example.myapplication.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.data.api.model.Track
import com.example.myapplication.databinding.SelectedTrackFragmentBinding
import com.example.myapplication.di.DaggerAppComponent
import com.example.myapplication.presentation.fragments.TrackListFragment.Companion.TRACK_ID_KEY
import com.example.myapplication.presentation.viewmodel.TrackViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectedTrackFragment : Fragment() {

    @Inject
    lateinit var glide: RequestManager

    private val binding by lazy { SelectedTrackFragmentBinding.inflate(layoutInflater) }

    private val trackViewModel: TrackViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.getAppComponent().inject(this)


        val trackId = this.arguments?.getInt(TRACK_ID_KEY)

        trackId?.let {
            lifecycleScope.launch {
                val track = trackViewModel.getTrackByTrackId(it)
                if (track.trackId != -1) bindTrack(track)
            }
        }

        binding.backButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        return binding.root
    }


    private fun bindTrack(track: Track) {
        with(binding) {
            trackId.text = track.trackId.toString()
            trackName.text = track.trackName
            collectionName.text = track.collectionName
            artistName.text = track.artistName

            glide
                .load(track.artworkUrl100)
                .apply(options)
                .into(image100)
        }

    }

    private val options by lazy {
        RequestOptions()
            .centerInside()
            .placeholder(R.drawable.no_track_48dp)
            .error(R.drawable.no_track_48dp)
    }


}