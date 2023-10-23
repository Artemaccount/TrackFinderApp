package com.example.myapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.data.api.Track
import com.example.myapplication.databinding.RecyclerViewItemBinding

class TracksAdapter :
    ListAdapter<Track, TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = RecyclerViewItemBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        viewHolder.bind(track)
    }
}

class TrackViewHolder(private val binding: RecyclerViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val options by lazy {
        RequestOptions()
            .centerInside()
            .placeholder(R.drawable.no_track_48dp)
            .error(R.drawable.no_track_48dp)
    }

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.albumAuthor.text = track.artistName
        setPic(track.artworkUrl60)
    }
    private fun setPic(url: String) {
        Glide.with(binding.root)
            .load(url)
            .apply(options)
            .into(binding.albumImage)
    }
}

class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean =
        oldItem.trackId == newItem.trackId

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem == newItem

}