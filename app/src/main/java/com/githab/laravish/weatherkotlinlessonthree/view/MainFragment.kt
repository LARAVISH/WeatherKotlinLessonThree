package com.githab.laravish.weatherkotlinlessonthree.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.githab.laravish.weatherkotlinlessonthree.R
import com.githab.laravish.weatherkotlinlessonthree.databinding.FragmentMainBinding
import com.githab.laravish.weatherkotlinlessonthree.model.Weather
import com.githab.laravish.weatherkotlinlessonthree.view.adapter.MainFragmentAdapter
import com.githab.laravish.weatherkotlinlessonthree.view.adapter.MyOnClickListener
import com.githab.laravish.weatherkotlinlessonthree.view.details.DetailsFragment
import com.githab.laravish.weatherkotlinlessonthree.view.details.KEY_ARG
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.AppState
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.MainViewModel

class MainFragment : Fragment(), MyOnClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() {
            return _binding!!
        }

    private lateinit var viewModel: MainViewModel
    private val adapter: MainFragmentAdapter = MainFragmentAdapter(this)
    private var isRus = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        binding.mainFragmentRecycler.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { setRequest() }
    }

    private fun setRequest() = with(binding) {
        mainFragmentFAB.setImageResource(R.drawable.ic_baseline_ads_click_24)
        isRus = !isRus
        if (isRus) {
            viewModel.getWeatherFromLocalServerRusCities()
            mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalServerWorldCities()
            mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
    }

    private fun renderData(appAState: AppState) {

        when (appAState) {
            is AppState.Error ->
                AppState.Error(IllegalArgumentException("Error"))

            is AppState.Loading ->
                AppState.Loading("Update")
            is AppState.Success -> adapter.setData(appAState.weather)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_ARG, weather)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, DetailsFragment.newInstance(bundle))
            .addToBackStack("DetailsFragment")
            .commit()
    }
}
