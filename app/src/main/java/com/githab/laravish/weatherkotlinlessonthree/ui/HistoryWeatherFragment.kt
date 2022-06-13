package com.githab.laravish.weatherkotlinlessonthree.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.githab.laravish.weatherkotlinlessonthree.databinding.FragmentHistoryWeatherBinding
import com.githab.laravish.weatherkotlinlessonthree.ui.adapter.HistoryAdapter
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.AppState
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryWeatherFragment : Fragment() {

    private var _binding: FragmentHistoryWeatherBinding? = null
    private val binding: FragmentHistoryWeatherBinding
        get() = _binding!!
    private val viewModel: HistoryViewModel by viewModel()
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        historyFragmentRecyclerview.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
            }
            AppState.Loading -> {
            }
            is AppState.Success -> {
                adapter.setData(appState.weatherData)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HistoryWeatherFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}