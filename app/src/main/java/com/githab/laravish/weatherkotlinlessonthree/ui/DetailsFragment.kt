package com.githab.laravish.weatherkotlinlessonthree.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.githab.laravish.weatherkotlinlessonthree.KEY_ARG
import com.githab.laravish.weatherkotlinlessonthree.R
import com.githab.laravish.weatherkotlinlessonthree.data.Weather
import com.githab.laravish.weatherkotlinlessonthree.databinding.FragmentDetailsBinding
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.AppState
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_ARG)?.let {
            renderStaticData(it)
            viewModel.weatherLiveData.observe(viewLifecycleOwner) { appState ->
                renderDynamicData(appState)
            }
            viewModel.loadData(it.city.lat, it.city.lon)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderStaticData(weather: Weather) = with(binding) {
        val city = weather.city
        cityName.text = city.name
        cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.lat.toString(),
            city.lon.toString()
        )
    }

    private fun renderDynamicData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Error -> {
                mainView.visibility = View.INVISIBLE
                progressBar.visibility = View.GONE
                errorTV.visibility = View.VISIBLE
            }
            AppState.Loading -> {
                mainView.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                progressBar.visibility = View.GONE
                mainView.visibility = View.VISIBLE
                setWeather(appState)
                setImage(appState)
            }
        }
    }

    private fun FragmentDetailsBinding.setWeather(
        appState: AppState.Success,
    ) {
        temperatureValue.text = appState.weatherData[0].temperature.toString()
        feelsLikeValue.text = appState.weatherData[0].feelsLike.toString()
        weatherCondition.text = appState.weatherData[0].condition
    }

    private fun FragmentDetailsBinding.setImage(
        appState: AppState.Success,
    ) = imageView.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png") {
        crossfade(true)
        placeholder(R.drawable.ic_launcher_background)
        iconWeather.loadSvg("https://yastatic.net/weather/i/icons/funky/dark/${appState.weatherData[0].icon}.svg")
    }

    private fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) = DetailsFragment().apply { arguments = bundle }
    }
}