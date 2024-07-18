package com.hazrat.qiblafinder

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import android.graphics.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun CompassView(qiblaDirection: Float, currentDirection: Float) {
    val compassBg = painterResource(id = R.drawable.compass_base)
    val qiblaIcon = ImageBitmap.imageResource(id = R.drawable.qibla_icon)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
        )

        Box(modifier = Modifier.fillMaxHeight(0.5f)) {
            // Custom compass background
            Image(
                painter = compassBg,
                contentDescription = "Compass Background",
                modifier = Modifier.fillMaxSize()
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val compassCenter = Offset(size.width / 2, size.height / 2)
                val compassRadius = size.minDimension / 2.5f

                // Draw the current direction needle
                rotate(degrees = currentDirection, pivot = compassCenter) {
                    drawLine(
                        color = Color.Green,
                        start = compassCenter,
                        end = Offset(compassCenter.x, compassCenter.y - compassRadius),
                        strokeWidth = 8f
                    )
                }

                // Draw the Qibla direction needle
                rotate(degrees = qiblaDirection - currentDirection, pivot = compassCenter) {
                    drawLine(
                        color = Color.Red,
                        start = compassCenter,
                        end = Offset(compassCenter.x, compassCenter.y - compassRadius),
                        strokeWidth = 8f
                    )

                    // Draw the Qibla icon
                    drawImage(
                        image = qiblaIcon,
                        topLeft = Offset(
                            compassCenter.x - qiblaIcon.width / 2,
                            compassCenter.y - compassRadius - qiblaIcon.height / 2
                        )
                    )
                }
            }
        }
    }

}


