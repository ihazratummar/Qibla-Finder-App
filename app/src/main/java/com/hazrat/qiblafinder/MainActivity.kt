package com.hazrat.qiblafinder

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.things.update.UpdateManager
import com.hazrat.qiblafinder.service.CompassSensorManager
import com.hazrat.qiblafinder.service.LocationManager
import com.hazrat.qiblafinder.service.PermissionsManager
import com.hazrat.qiblafinder.ui.theme.QiblaFinderTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(){

    @Inject
    lateinit var locationManager: LocationManager


    @Inject
    lateinit var compassSensorManager: CompassSensorManager

    private lateinit var permissionsManager: PermissionsManager
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionsManager = PermissionsManager(this)
        permissionsManager.onPermissionGranted = {
            locationManager.getLastKnownLocation()
        }
        permissionsManager.checkAndRequestLocationPermission()

        locationManager.onLocationReceived = { location ->
            val qiblaDirection = calculateQiblaDirection(location.latitude, location.longitude).toFloat()
            Log.d("MainActivity Qibla", "New Qibla Direction: $qiblaDirection")
            viewModel.updateQiblaDirection(qiblaDirection)
        }

        compassSensorManager.onDirectionChanged = { direction ->
            Log.d("MainActivity Current", "New Current Direction: $direction")
            viewModel.updateCurrentDirection(direction)
        }

        setContent {
            QiblaFinderTheme {
                val state = viewModel.qiblaState.collectAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QiblaScreen(
                        modifier = Modifier.padding(innerPadding),
                        qiblaDirection = state.value.qiblaDirection,
                        currentDirection = state.value.currentDirection
                    )
                }
            }
        }
        compassSensorManager.registerListeners()
    }

}

