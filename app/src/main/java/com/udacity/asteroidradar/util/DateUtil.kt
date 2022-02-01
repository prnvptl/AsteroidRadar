package com.udacity.asteroidradar.util

import java.text.SimpleDateFormat

fun getTimeStampStartOfDay(dateString: String) : Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val dateWithoutTime = sdf.parse(dateString)
    return dateWithoutTime.time
}