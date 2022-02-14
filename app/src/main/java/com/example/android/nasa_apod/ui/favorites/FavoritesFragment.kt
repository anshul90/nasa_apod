package com.example.android.nasa_apod.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.nasa_apod.R
import com.example.android.nasa_apod.databinding.FragmentMainBinding
import com.example.android.nasa_apod.model.FavoritesEntity
import com.example.android.nasa_apod.ui.mainActivity.MainActivity

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoritesAdapter: FavoritesAdapter

    private lateinit var list: List<FavoritesEntity>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        list = viewModel.loadData()
        setupBinding()
    }

    private fun setupBinding() {
        binding.fabIcon.visibility = View.GONE
        binding.swipeRefreshLayout.isEnabled = false
        favoritesAdapter = FavoritesAdapter { favsEntity ->
            (requireActivity() as MainActivity).navController.navigate(
                R.id.action_fav_to_detail,
                bundleOf("favs" to favsEntity)
            )
        }

        binding.recyclerView.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        binding.recyclerView.isVisible = !list.isNullOrEmpty()
        binding.layoutGroup.isVisible = !binding.recyclerView.isVisible
        favoritesAdapter.submitList(list)
    }
}