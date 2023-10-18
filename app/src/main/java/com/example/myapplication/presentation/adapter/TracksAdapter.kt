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
        viewHolder.setPic(track)
    }
}

class TrackViewHolder(private val binding: RecyclerViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.albumAuthor.text = track.artistName
    }


    fun setPic(track: Track) {
        val options: RequestOptions = RequestOptions()
            .centerInside()
            .placeholder(R.drawable.no_track_48dp)
            .error(R.drawable.no_track_48dp)
        Glide.with(binding.root)
            .load(track.artworkUrl60)
            .apply(options)
            .into(binding.albumImage)
    }


}

class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean =
        oldItem.trackId == newItem.trackId

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem == newItem

}