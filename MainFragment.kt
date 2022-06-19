package com.githab.laravish.weatherkotlinlessonthree.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.githab.laravish.weatherkotlinlessonthree.*
import com.githab.laravish.weatherkotlinlessonthree.data.City
import com.githab.laravish.weatherkotlinlessonthree.data.Weather
import com.githab.laravish.weatherkotlinlessonthree.databinding.FragmentMainBinding
import com.githab.laravish.weatherkotlinlessonthree.ui.adapter.MainFragmentAdapter
import com.githab.laravish.weatherkotlinlessonthree.ui.adapter.MyOnClickListener
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.AppState
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), MyOnClickListener, CoroutineScope by MainScope() {


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
        mainFragmentFABlocation.setOnClickListener { checkLocationPermission() }
    }

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.dialog_message_no_gps),
                    Toast.LENGTH_LONG).show()
            }
        }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let { context ->
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    REFRESH_PERIOD, MIN_DISTANCE, onLocationListener)
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location == null) {
                    Toast.makeText(requireContext(), getString(R.string.dialog_title_gps_turned_off
                    ), Toast.LENGTH_LONG).show()
                } else {
                    getAddressAsync(context, location)
                }
            }
        }
    }

    private val onLocationListener =
        LocationListener { location -> context?.let { getAddressAsync(it, location) } }

    private fun getAddressAsync(context: Context, location: Location) {
        val geocoder = Geocoder(context)
        launch {
            val job = async(Dispatchers.IO) {
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            }
            val address = job.await()
            if (isActive) {
                showAddressDialog(address[0].getAddressLine(0), location)
            }
        }
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    openDetailsFragment(Weather(City(address,
                        location.latitude,
                        location.longitude)))
                }.setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }
    }

    private fun openDetailsFragment(weather: Weather) {
        activity?.supportFragmentManager?.let { manager ->
            val bundle = Bundle().apply {
                putParcelable(KEY_ARG, weather)
            }
            manager.beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(bundle))
                .addToBackStack("DetailsFragment")
                .commit()
        }
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
        cancel()
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
                })).addToBackStack("DetailsServiceFragment").commitAllowingStateLoss()
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











