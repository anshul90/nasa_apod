package com.example.android.nasa_apod.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nasa_apod.R
import com.example.android.nasa_apod.databinding.FragmentMainBinding
import com.example.android.nasa_apod.di.RoomModule
import com.example.android.nasa_apod.domain.util.*
import com.example.android.nasa_apod.ui.mainActivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainAdapter: MainAdapter

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
        setupBinding()
        setupObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.refreshData()
                true
            }
            R.id.menu_favorites -> {
                (requireActivity() as MainActivity).navController.navigate(
                    R.id.action_main_to_favs)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        if (activity?.let { RoomModule.provideDatabase(it).apodListDao().getAll() } != null) {
            viewModel.loadData()
        } else viewModel.refreshData()
    }

    private fun setupBinding() {
        mainAdapter = MainAdapter { apodEntity ->
            (requireActivity() as MainActivity).navController.navigate(
                R.id.action_main_to_detail,
                bundleOf("apod" to apodEntity)
            )
        }
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refreshData() }
        binding.recyclerView.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && binding.fabIcon.isShown)
                        binding.fabIcon.hide()
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE)
                        binding.fabIcon.show()
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
        binding.fabIcon.setOnClickListener {
            childFragmentManager.showCalender {
                viewModel.updateDate(it)
            }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.also {
            it.launchWhenStarted {
                viewModel.errorEvents.collect { event ->
                    when (event) {
                        is Event.ShowErrorMessage -> showSnackBarError(
                            getString(
                                R.string.error_event,
                                event.error.localizedMessage ?: getString(R.string.unknown_error)
                            )
                        )
                    }
                }
            }
            it.launchWhenStarted {
                viewModel.lists.collect { resource ->
                    val result = resource ?: return@collect
                    binding.swipeRefreshLayout.isRefreshing = resource is Resource.Loading
                    binding.recyclerView.isVisible = !result.data.isNullOrEmpty()
                    binding.layoutGroup.isVisible = !binding.recyclerView.isVisible
                    mainAdapter.submitList(result.data)
                }
            }
        }
    }
}