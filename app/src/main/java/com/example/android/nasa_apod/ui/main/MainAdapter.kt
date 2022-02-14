package com.example.android.nasa_apod.ui.main

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.nasa_apod.R
import com.example.android.nasa_apod.databinding.MainRowItemBinding
import com.example.android.nasa_apod.di.RoomModule
import com.example.android.nasa_apod.model.ApodEntity
import com.example.android.nasa_apod.model.FavoritesEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainAdapter(private val onItemClick: (ApodEntity) -> Unit) :
    ListAdapter<ApodEntity, MainViewHolder>(MainAdapterComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(
            MainRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val context: Context = holder.itemView.getContext()

        getItem(position)?.let {
            holder.bind(it, context)
        }
    }

}

class MainViewHolder(
    private val binding: MainRowItemBinding,
    private val onItemClick: (ApodEntity) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(entity: ApodEntity, context: Context) {
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
                /*binding.videoView.setVideoPath(entity.url)
                val uri: Uri = Uri.parse(entity.url)
                with(binding) {
                    videoView.setMediaController(MediaController(context))
                    videoView.setVideoURI(uri)
                    videoView.requestFocus()
                    videoView.start()
                }*/
            }

            binding.favouriteTextView.setOnClickListener {
                val favEntity = FavoritesEntity(0, entity.copyright,
                    entity.date, entity.explanation, entity.hdurl, entity.mediaType, entity.serviceVersion,
                    entity.title, entity.url, true)

                GlobalScope.launch {
                    RoomModule.provideDatabase(context).favoritesListDao().saveFavorites(favEntity)
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, context.getString(R.string.added_favorites), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}

class MainAdapterComparator : DiffUtil.ItemCallback<ApodEntity>() {
    override fun areItemsTheSame(oldItem: ApodEntity, newItem: ApodEntity): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: ApodEntity, newItem: ApodEntity): Boolean =
        oldItem == newItem

}