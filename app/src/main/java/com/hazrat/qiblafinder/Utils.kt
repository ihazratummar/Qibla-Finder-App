package com.hazrat.qiblafinder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Hazrat Ummar Shaikh
 */

fun calculateQiblaDirection(latitude: Double, longitude: Double): Double {
    val kaabaLatitude = 21.4225
    val kaabaLongitude = 39.8262

    val latDifference = Math.toRadians(kaabaLatitude - latitude)
    val lonDifference = Math.toRadians(kaabaLongitude - longitude)
    val y = sin(lonDifference) * cos(Math.toRadians(kaabaLatitude))
    val x = cos(Math.toRadians(latitude)) * sin(Math.toRadians(kaabaLatitude)) -
            sin(Math.toRadians(latitude)) * cos(Math.toRadians(kaabaLatitude)) * cos(lonDifference)
    return (Math.toDegrees(atan2(y, x)) + 360) % 360
}

fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
    val drawable: Drawable = ContextCompat.getDrawable(context, drawableId)!!
    return drawable.toBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
}



fun vibrateDevice(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    if (vibrator != null && vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    200, // Vibration duration in milliseconds
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100) // Vibrate for 200 milliseconds
        }
    }
}