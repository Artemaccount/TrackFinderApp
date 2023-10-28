package com.example.myapplication.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.data.api.model.Track
import com.example.myapplication.databinding.SelectedTrackFragmentBinding
import com.example.myapplication.presentation.viewmodel.TrackViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectedTrackFragment : Fragment() {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var router: Router

    private val trackId: Int
        get() = requireArguments().getInt(TRACK_ID)

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

        lifecycleScope.launch {
            val track = trackViewModel.getTrackByTrackId(trackId)
            bindTrack(track)
        }

        binding.backButton.setOnClickListener {
            router.backTo(Screens.trackList())
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


    companion object {
        private const val TRACK_ID = "track_id"
        fun getNewInstance(trackId: Int): SelectedTrackFragment {
            return SelectedTrackFragment().apply {
                arguments = bundleOf(TRACK_ID to trackId)
            }
        }
    }
}