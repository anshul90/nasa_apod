package com.example.android.nasa_apod.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.nasa_apod.databinding.MainRowItemBinding
import com.example.android.nasa_apod.model.FavoritesEntity

class FavoritesAdapter(private val onItemClick: (FavoritesEntity) -> Unit) :
    ListAdapter<FavoritesEntity, FavoritesViewHolder>(FavoritesAdapterComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder =
        FavoritesViewHolder(
            MainRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}

class FavoritesViewHolder(
    private val binding: MainRowItemBinding,
    private val onItemClick: (FavoritesEntity) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(entity: FavoritesEntity) {
        binding.apply {
            root.setOnClickListener { onItemClick(entity) }
            Glide.with(itemView)
                .load(entity.hdurl)
                .into(binding.imageView)
            binding.copyrightTextView.text = entity.copyright ?: "Unknown"
            binding.titleTextView.text = entity.title ?: "Unknown"
            binding.dateTextView.text = entity.date ?: "Unknown"

            if (entity.hdurl.isNullOrBlank()) {
                binding.imageView.visibility = View.GONE
                binding.videoView.visibility = View.VISIBLE
                /* binding.videoView.setVideoPath(entity.url)
                 binding.videoView.start()*/
            }
        }

        binding.favouriteTextView.visibility = View.GONE
    }
}

class FavoritesAdapterComparator : DiffUtil.ItemCallback<FavoritesEntity>() {
    override fun areItemsTheSame(oldItem: FavoritesEntity, newItem: FavoritesEntity): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: FavoritesEntity, newItem: FavoritesEntity): Boolean =
        oldItem == newItem

}