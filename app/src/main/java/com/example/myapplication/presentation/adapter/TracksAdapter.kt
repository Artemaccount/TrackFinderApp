package com.example.myapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.data.api.model.Track
import com.example.myapplication.databinding.RecyclerViewItemBinding
import com.example.myapplication.di.AppComponent
import javax.inject.Inject


interface OnInteractionListener {
    fun select(track: Track)
}

class TracksAdapter(
    private val appComponent: AppComponent,
    private val onInteractionListener: OnInteractionListener,
) :
    ListAdapter<Track, TracksAdapter.TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = RecyclerViewItemBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return TrackViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(viewHolder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        viewHolder.bind(track)
    }

    inner class TrackViewHolder(
        private val binding: RecyclerViewItemBinding,
        private val onInteractionListener: OnInteractionListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            appComponent.inject(this)
        }

        @Inject
        lateinit var glide: RequestManager

        private val options by lazy {
            RequestOptions()
                .centerInside()
                .placeholder(R.drawable.no_track_48dp)
                .error(R.drawable.no_track_48dp)
        }

        fun bind(track: Track) {
            with(binding) {
                trackName.text = track.trackName
                albumAuthor.text = track.artistName
                setPic(track.artworkUrl60)
                root.setOnClickListener {
                    onInteractionListener.select(track)
                }
            }
        }

        private fun setPic(url: String) {
            glide
                .load(url)
                .apply(options)
                .into(binding.albumImage)
        }
    }

}


class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean =
        oldItem.trackId == newItem.trackId

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem == newItem

}