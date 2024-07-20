package com.hazrat.qiblafinder


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hazrat.qiblafinder.ui.theme.Hidaya
import com.hazrat.qiblafinder.util.vibrateDevice

/**
 * @author Hazrat Ummar Shaikh
 */


/**
 * QiblaScreen is a composable function that displays a compass indicating the Qibla direction.
 * It also vibrates the device when the user faces the Qibla.
 *
 * @param modifier The modifier to be applied to the QiblaScreen.
 * @param navController The NavHostController for navigation.
 * @param qiblaDirection The direction of the Qibla in degrees.
 * @param currentDirection The current direction of the device in degrees.
 */

@Composable
fun QiblaScreen(
    modifier: Modifier = Modifier,
    qiblaDirection: Float,
    currentDirection: Float,
) {


    val context = LocalContext.current

    val compassBgBitmap =
        remember { drawableToBitmap(context, R.drawable.compass3).asImageBitmap() }
    val qiblaIconBitmap =
        remember { drawableToBitmap(context, R.drawable.qiblaiconpoint).asImageBitmap() }
    val needleBitmap = remember { drawableToBitmap(context, R.drawable.needles).asImageBitmap() }
    val goldQaba = remember { drawableToBitmap(context, R.drawable.goldqaba).asImageBitmap() }

    val minTolerance = 3f // Adjusted tolerance range
    val maxTolerance = 3.5f // Adjusted tolerance range

    val directionDifference = qiblaDirection - currentDirection
    val normalizedDifference = (directionDifference + 360) % 360

    val isFacingQibla = (
            (normalizedDifference in 0.0..maxTolerance.toDouble()) ||
                    (normalizedDifference >= 360 - minTolerance && normalizedDifference <= 360)
            )

    var hasVibrated by remember { mutableStateOf(false) }

    // Vibrate when facing Qibla and not already vibrated
    if (isFacingQibla && !hasVibrated) {
        vibrateDevice(context, 200)
        hasVibrated = true // Set to true to prevent continuous vibration
    } else if (!isFacingQibla) {
        hasVibrated = false // Reset when not facing Qibla
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Qibla: ${qiblaDirection.toInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Hidaya
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (isFacingQibla) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(id = R.drawable.goldqaba),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            } else {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.arrowup),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .padding(10.dp)
            ) {
                // Canvas for compass
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val compassCenter = Offset(size.width / 2, size.height / 2)
                    val compassRadius = size.minDimension / 2.5f

                    // Rotate the entire compass background image
                    rotate(degrees = -currentDirection, pivot = compassCenter) {
                        drawImage(
                            image = compassBgBitmap,
                            topLeft = Offset(
                                compassCenter.x - compassBgBitmap.width / 2,
                                compassCenter.y - compassBgBitmap.height / 2
                            )
                        )
                    }
                    // Draw the Qibla direction needle
                    rotate(degrees = qiblaDirection - currentDirection, pivot = compassCenter) {
                        val needleStartY = compassCenter.y - needleBitmap.height / 1.1f
                        drawImage(
                            image = needleBitmap,
                            topLeft = Offset(
                                compassCenter.x - needleBitmap.width / 2f,
                                needleStartY
                            )
                        )

                        // Draw the Qibla icon
                        if (!isFacingQibla) {
                            drawImage(
                                image = qiblaIconBitmap,
                                topLeft = Offset(
                                    compassCenter.x - qiblaIconBitmap.width / 2,
                                    compassCenter.y - compassRadius - qiblaIconBitmap.height / 1.1f
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}



