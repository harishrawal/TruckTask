package com.telematics.asset.trucktask.model


import com.google.gson.annotations.SerializedName

data class VehicleMake(
    @SerializedName("images")
    val images: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("value")
    val value: Int
)