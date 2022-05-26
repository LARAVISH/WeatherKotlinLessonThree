package com.githab.laravish.weatherkotlinlessonthree.view.details


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.githab.laravish.weatherkotlinlessonthree.databinding.FragmentDetailsBinding
import com.githab.laravish.weatherkotlinlessonthree.model.Weather


const val KEY_ARG = "KEY_ARG"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { it.getParcelable<Weather>(KEY_ARG)?.run { setTextWeather(this) } }
    }

    private fun setTextWeather(
        weather: Weather
    ) = with(binding) {
        cityName.text = weather.city.name
        "${weather.city.lat} ${weather.city.lon}".also { cityCoordinates.text = it }
        temperatureValue.text = "${weather.temperature}"
        feelsLikeValue.text = "${weather.feel_likes}"
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) = DetailsFragment().apply { arguments = bundle }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
