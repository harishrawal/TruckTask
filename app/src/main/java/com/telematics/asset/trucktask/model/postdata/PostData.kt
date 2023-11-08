package com.telematics.asset.trucktask.model.postdata


import com.google.gson.annotations.SerializedName

data class PostData(

    val clientid :Int ,
    val enterprise_code: Int ,
    val mno: String ,
    val passcode: Int
)