package com.githab.laravish.weatherkotlinlessonthree.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.githab.laravish.weatherkotlinlessonthree.DATA_SET_KEY
import com.githab.laravish.weatherkotlinlessonthree.KEY_ARG
import com.githab.laravish.weatherkotlinlessonthree.R
import com.githab.laravish.weatherkotlinlessonthree.data.Weather
import com.githab.laravish.weatherkotlinlessonthree.databinding.FragmentMainBinding
import com.githab.laravish.weatherkotlinlessonthree.ui.adapter.MainFragmentAdapter
import com.githab.laravish.weatherkotlinlessonthree.ui.adapter.MyOnClickListener
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.AppState
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), MyOnClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() {
            return _binding!!
        }

    private val viewModel: MainViewModel by viewModel()
    private val adapter: MainFragmentAdapter by lazy { MainFragmentAdapter(this) }
    private var isRus = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.liveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getWeatherFromLocalServerRusCities()
        loadDataSet()
        initDataSet()
    }

    private fun initView() {
        mainFragmentRecycler.adapter = adapter
        mainFragmentFAB.setOnClickListener { setRequest() }
    }

    private fun setRequest() = with(binding) {
        isRus = !isRus
        initDataSet()
    }

    private fun renderData(appAState: AppState) = with(binding) {
        mainFragmentLoadingLayout.visibility = View.VISIBLE
        when (appAState) {
            is AppState.Error -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                AppState.Error(IllegalArgumentException(getString(R.string.error_happened)))
                root.apply {
                    snack(getString(R.string.error_happened)) {
                        setAction(getString(R.string.try_again)) {
                            setRequest()
                        }
                    }
                }
            }
            is AppState.Loading -> {
                mainFragmentLoadingLayout.visibility = View.VISIBLE
                AppState.Loading
            }
            is AppState.Success -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setData(appAState.weatherData)
                binding.root.apply {
                    snack(getString(R.string.success)) {
                        setAction(getString(R.string.click_hear)) {
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    private inline fun View.snack(
        message: String,
        length: Int = Snackbar.LENGTH_LONG,
        f: Snackbar.() -> Unit,
    ) {
        val snack = Snackbar.make(this, message, length)
        snack.f()
        snack.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onClick(weather: Weather) {
        activity?.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(
                        KEY_ARG, weather
                    )
                })).addToBackStack("DetailsServiceFragment").commit()
        }
    }

    private fun saveDataSetToDisk() {
        val editor =
            activity?.getSharedPreferences("namedPreferences", Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(DATA_SET_KEY, isRus)
        editor?.apply()
    }

    private fun loadDataSet() {
        activity?.let {
            isRus = activity?.getSharedPreferences("namedPreferences", Context.MODE_PRIVATE)
                ?.getBoolean(DATA_SET_KEY, true)
                ?: true
        }
    }

    private fun initDataSet() {
        if (isRus) {
            viewModel.getWeatherFromLocalServerRusCities()
            mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalServerWorldCities()
            mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
        saveDataSetToDisk()
    }

}




