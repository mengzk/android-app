package com.ql.health.util

import android.app.Activity
import android.util.DisplayMetrics

object DisplayUtils {

    fun getScreenSize(context: Activity): String {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val density = displayMetrics.density
        val screenWidthDp = screenWidth / density
        val screenHeightDp = screenHeight / density

        // Log or use the screen width and height as needed
        println("---> Screen Width: $screenWidth, Height: $screenHeight, Density: $density")
        println("---> Screen in dp Width : $screenWidthDp, Height: $screenHeightDp")
        return "Screen Width: $screenWidth, Height: $screenHeight, Density: $density"
    }
}