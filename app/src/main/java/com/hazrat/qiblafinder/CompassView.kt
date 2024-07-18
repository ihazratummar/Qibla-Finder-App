package com.hazrat.qiblafinder

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun CompassView(
    modifier: Modifier = Modifier,
    qiblaDirection: Float, currentDirection: Float
) {
    val context = LocalContext.current
    val compassBgBitmap =
        remember { drawableToBitmap(context, R.drawable.compass3).asImageBitmap() }
    val qiblaIconBitmap =
        remember { drawableToBitmap(context, R.drawable.qiblaicon).asImageBitmap() }
    val needleBitmap = remember { drawableToBitmap(context, R.drawable.needles).asImageBitmap() }

    val minTolerance = 3.1f // Adjusted tolerance range
    val maxTolerance = 3.4f // Adjusted tolerance range

    val directionDifference = qiblaDirection - currentDirection
    val normalizedDifference = (directionDifference + 360) % 360

    val isFacingQibla = (
            (normalizedDifference in 0.0..maxTolerance.toDouble()) ||
                    (normalizedDifference >= 360 - minTolerance && normalizedDifference <= 360)
            )

    var hasVibrated by remember { mutableStateOf(false) }

    // Vibrate when facing Qibla and not already vibrated
    if (isFacingQibla && !hasVibrated) {
        vibrateDevice(context)
        hasVibrated = true // Set to true to prevent continuous vibration
    } else if (!isFacingQibla) {
        hasVibrated = false // Reset when not facing Qibla
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isFacingQibla) {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp)
                    .shadow(
                        elevation = 5.dp,
                        ambientColor = Color.Cyan,
                        spotColor = Color.Cyan
                    )
                    .background(
                        color = Color(0xff01716a),
                        shape = CircleShape
                    ),
                shape = CircleShape,
                border = BorderStroke(1.dp, color = Color(0xff01716a)),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {

            }
        } else {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, color = Color.Black),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {

            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(10.dp)
        ) {
            // Down arrow icon


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

                // Draw the current direction needle
//                rotate(degrees = currentDirection, pivot = compassCenter) {
//                    drawLine(
//                        color = Color.Green,
//                        start = compassCenter,
//                        end = Offset(compassCenter.x, compassCenter.y - compassRadius),
//                        strokeWidth = 8f
//                    )
//                }

                // Draw the Qibla direction needle
                rotate(degrees = qiblaDirection - currentDirection, pivot = compassCenter) {
                    val needleStartY = compassCenter.y - needleBitmap.height / 1.1f
                    drawImage(
                        image = needleBitmap,
                        topLeft = Offset(
                            compassCenter.x - needleBitmap.width / 2,
                            needleStartY
                        )
                    )

                    // Draw the Qibla icon
                    drawImage(
                        image = qiblaIconBitmap,
                        topLeft = Offset(
                            compassCenter.x - qiblaIconBitmap.width / 2,
                            compassCenter.y - compassRadius - qiblaIconBitmap.height / 1
                        ),
                    )
                }
            }
        }
    }
}



