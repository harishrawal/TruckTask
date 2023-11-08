package com.telematics.asset.trucktask.model


import com.google.gson.annotations.SerializedName

data class VehicleDataResponse(

    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("vehicle_type")
    val vehicleType: List<VehicleType>,

    @SerializedName("vehicle_capacity")
    val vehicleCapacity: List<VehicleCapacity>,

    @SerializedName("vehicle_make")
    val vehicleMake: List<VehicleMake>,

    @SerializedName("manufacture_year")
    val manufactureYear: List<ManufactureYear>,

    @SerializedName("fuel_type")
    val fuelType: List<FuelType>

)