package com.githab.laravish.weatherkotlinlessonthree.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.githab.laravish.weatherkotlinlessonthree.R
import com.githab.laravish.weatherkotlinlessonthree.databinding.FragmentMainBinding
import com.githab.laravish.weatherkotlinlessonthree.model.Weather
import com.githab.laravish.weatherkotlinlessonthree.view.adapter.MainFragmentAdapter
import com.githab.laravish.weatherkotlinlessonthree.view.adapter.MyOnClickListener
import com.githab.laravish.weatherkotlinlessonthree.view.details.DetailsFragment
import com.githab.laravish.weatherkotlinlessonthree.view.details.KEY_ARG
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
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.liveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getWeatherFromLocalServerRusCities()
    }

    private fun initView() {
        mainFragmentRecycler.adapter = adapter
        mainFragmentFAB.setOnClickListener { setRequest() }
    }

    private fun setRequest() = with(binding) {
        isRus = !isRus
        if (isRus) {
            viewModel.getWeatherFromLocalServerRusCities()
            mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalServerWorldCities()
            mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
    }

    private fun renderData(appAState: AppState) = with(binding) {
        mainFragmentLoadingLayout.visibility = View.VISIBLE
        when (appAState) {
            is AppState.Error -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                AppState.Error(IllegalArgumentException(getString(R.string.error)))
                root.apply {
                    snack(getString(R.string.error)) {
                        setAction(getString(R.string.try_again)) {
                            setRequest()
                        }
                    }
                }
            }
            is AppState.Loading -> {
                mainFragmentLoadingLayout.visibility = View.VISIBLE
                AppState.Loading(getString(R.string.loading))
            }
            is AppState.Success -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setData(appAState.weather)
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
        f: Snackbar.() -> Unit
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
                })).addToBackStack("DetailsFragment").commit()
        }
    }
}


