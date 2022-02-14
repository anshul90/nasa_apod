package com.example.android.nasa_apod.ui.details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.android.nasa_apod.R
import com.example.android.nasa_apod.databinding.FragmentDetailsBinding
import com.example.android.nasa_apod.model.ApodEntity
import com.example.android.nasa_apod.model.FavoritesEntity


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBinding() {
        if (arguments?.getParcelable<ApodEntity>("apod") != null) {
            arguments?.getParcelable<ApodEntity>("apod")?.let {
                binding.titleTextView.text = it.title ?: getString(R.string.unknown_error)
                binding.copyrightTextView.text = it.copyright ?: getString(R.string.unknown_error)
                binding.copyrightTextView.visibility = if (it.copyright.isNullOrBlank()) View.GONE else View.VISIBLE
                binding.dateTextView.text = it.date ?: getString(R.string.unknown_error)
                binding.descriptionTextView.text = it.explanation ?: getString(R.string.unknown_error)
                Glide.with(binding.root)
                    .load(it.hdurl)
                    .into(binding.imageView)

                if (it.hdurl.isNullOrBlank()) {
                    binding.imageView.visibility = View.INVISIBLE
                    binding.videoView.visibility = View.VISIBLE
                    val uri: Uri = Uri.parse(it.url)
                    /*binding.videoView.setVideoURI(uri)
                    val mediaController = MediaController(activity)
                    mediaController.setAnchorView(binding.videoView)
                    mediaController.setMediaPlayer(binding.videoView)
                    binding.videoView.setMediaController(mediaController)
                    binding.videoView.setOnPreparedListener { binding.videoView.start() }*/
                }
            }
        } else if (arguments?.getParcelable<FavoritesEntity>("favs") != null) {
            arguments?.getParcelable<FavoritesEntity>("favs")?.let {
                binding.titleTextView.text = it.title ?: getString(R.string.unknown_error)
                binding.copyrightTextView.text = it.copyright ?: getString(R.string.unknown_error)
                binding.copyrightTextView.visibility = if (it.copyright.isNullOrBlank()) View.GONE else View.VISIBLE
                binding.dateTextView.text = it.date ?: getString(R.string.unknown_error)
                binding.descriptionTextView.text = it.explanation ?: getString(R.string.unknown_error)
                Glide.with(binding.root)
                    .load(it.hdurl)
                    .into(binding.imageView)

                if (it.hdurl.isNullOrBlank()) {
                    binding.imageView.visibility = View.INVISIBLE
                    binding.videoView.visibility = View.VISIBLE
                    val uri: Uri = Uri.parse(it.url)
                    /*binding.videoView.setVideoURI(uri)
                    val mediaController = MediaController(activity)
                    mediaController.setAnchorView(binding.videoView)
                    mediaController.setMediaPlayer(binding.videoView)
                    binding.videoView.setMediaController(mediaController)
                    binding.videoView.setOnPreparedListener { binding.videoView.start() }*/
                }
            }
        }
    }
}