package com.jpmc.weather.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jpmc.weather.App
import com.jpmc.weather.ui.theme.JPMCWeatherTheme
import com.jpmc.weather.viewmodel.WeatherViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: WeatherViewModel

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)

        /**
         * Checking Location permissions here, Ideally I would do check when the user is trying to
         * access location related feature in the app, but I need to automatically load weather info
         * for the user's location by default if permissions exist else user can use the search button
         * "Get Weather" anyways.
         */
        requestLocationPermission()

        setContent {
            JPMCWeatherTheme {
                WeatherAppUI(viewModel)
            }
        }
    }

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, initiate location retrieval logic
                fetchLocation()
            } else {
                // Permission denied, handle accordingly
                // do nothing, the user will search by entering the cityName
            }
        }

    // Request location permission
    private fun requestLocationPermission() {
        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    // Initiate location retrieval logic
    private fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.latitude?.let {
                    location.longitude.let { it1 ->
                        viewModel.fetchWeather(it, it1)
                    }
                }
            }
    }


}

@Composable
fun WeatherAppUI(viewModel: WeatherViewModel) {
    var cityName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            Arrangement.Center
        ) {
            Text(text = "Weather App Demo",fontWeight = FontWeight.Bold, fontSize = 28.sp)
        }
        // Input Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            Alignment.Bottom,

        ) {
            TextField(
                value = cityName,
                onValueChange = { cityName = it },
                label = { Text("Enter City Name") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    // Call ViewModel method to fetch weather data
                    viewModel.fetchWeather(cityName)
                },
                modifier = Modifier
                    .padding(0.dp)

            ) {
                Text("Get Weather")
            }
        }

        // Weather Data Section
        val weatherInfo = viewModel.weatherInfo.observeAsState()

        weatherInfo.value?.let { cityWeather ->
            if (cityWeather.errorMessage.isNullOrEmpty()) {
                val gradient = Brush.horizontalGradient(
                    0.0f to Color.White, 1.0f to Color.Cyan,
                    startX = 0.0f, endX = 1000.0f
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .background(gradient),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = cityWeather?.cityName?: cityName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "${ cityWeather.currTemp}°F", fontSize = 20.sp)
                        Image(
                            painter = rememberAsyncImagePainter(cityWeather.iconUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "High: ${cityWeather.daysHighTemp}°F", fontSize = 14.sp)
                        Text(text = "Low: ${cityWeather.daysLowTemp}°F", fontSize = 14.sp)
                    }
                }
            } else {
                showErrorMessage(message = cityWeather.errorMessage)
            }
        }
    }
}

@Composable
fun showErrorMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = message)
    }
}