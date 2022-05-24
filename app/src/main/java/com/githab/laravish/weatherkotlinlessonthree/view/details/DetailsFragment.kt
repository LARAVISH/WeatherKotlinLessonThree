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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.getParcelable<Weather>(KEY_ARG)
        if (weather != null) {
            setTextWeather(weather)
        }
    }

    private fun FragmentDetailsBinding.setTextWeather(
        weather: Weather
    ) {
        cityName.text = weather.city.name
        "${weather.city.lat} ${weather.city.lon}".also { cityCoordinates.text = it }
        temperatureValue.text = "${weather.temperature}"
        feelsLikeValue.text = "${weather.feel_likes}"
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
