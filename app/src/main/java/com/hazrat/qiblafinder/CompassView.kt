package com.hazrat.qiblafinder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.abs

@Composable
fun CompassView(qiblaDirection: Float, currentDirection: Float) {
    val context = LocalContext.current
    val compassBgBitmap = remember { drawableToBitmap(context, R.drawable.compass3).asImageBitmap() }
    val qiblaIconBitmap = remember { drawableToBitmap(context, R.drawable.qiblaicon).asImageBitmap() }
    val needleBitmap = remember { drawableToBitmap(context, R.drawable.needles).asImageBitmap() }

    // Define a tolerance range for the Qibla direction
    val minTolerance = 3.1f // Adjusted tolerance range
    val maxTolerance = 3.4f // Adjusted tolerance range

    // Calculate difference between current and Qibla direction for debug purposes
    val directionDifference = qiblaDirection / currentDirection
    val isFacingQibla = abs(directionDifference) < maxTolerance && abs(directionDifference) > minTolerance

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (isFacingQibla) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
            )
        } else {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.Red
            )
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
                    drawImage(
                        image = needleBitmap,
                        topLeft = Offset(
                            compassCenter.x - needleBitmap.width / 2,
                            compassCenter.y - compassRadius + 110  - needleBitmap.height / 25
                        )
                    )

                    // Draw the Qibla icon
                    drawImage(
                        image = qiblaIconBitmap,
                        topLeft = Offset(
                            compassCenter.x - qiblaIconBitmap.width / 2,
                            compassCenter.y - compassRadius - qiblaIconBitmap.height / 1
                        )
                    )
                }
            }
        }
    }
}

fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
    val drawable: Drawable = ContextCompat.getDrawable(context, drawableId)!!
    return drawable.toBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
}
