package com.udacity.asteroidradar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageOfTheDay(val url: String, val title: String, val mediaType: String) : Parcelable