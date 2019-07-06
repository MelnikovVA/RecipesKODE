package com.example.recipeskode.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(
    val uuid: String,
    val name: String,
    val images: Array<String>,
    val lastUpdated: Int,
    val description: String? = null,
    val instructions: String,
    val difficulty: Int
) : Parcelable