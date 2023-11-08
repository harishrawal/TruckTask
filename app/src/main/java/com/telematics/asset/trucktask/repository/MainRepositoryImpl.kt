package com.telematics.asset.trucktask.repository

import com.telematics.asset.trucktask.api.ApiService
import com.telematics.asset.trucktask.model.postdata.PostData
import javax.inject.Inject

/*TODO : In MainRepositoryImpl we have injected ApiService in the constructor
    itself so we do not have to create new instance of ApiService instead
     it is passed as a dependency to the ApiHelperImpl.
      In this way we can have only one instance of ApiService throughout the application lifecycle
      for any number of network calls.*/


class MainRepositoryImpl @Inject constructor(private val apiService: ApiService) : MainRepository {
    override suspend fun getVehicleDetails(postData: PostData) = apiService.getVehicleData(postData)
}