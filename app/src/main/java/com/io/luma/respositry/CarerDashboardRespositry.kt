package com.io.luma.respositry

import com.io.luma.model.CarerDashBoardResponseModel
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource

class CarerDashboardRespositry {
    private val api = ApiClient.api

    suspend fun getCarerDashBoard(): Resource<CarerDashBoardResponseModel> {
        return try {
            val response = api.getCarerDashBoard()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network Error: ${e.localizedMessage}")
        }
    }
}
//
//class CarerDashboardRespositry {
//    private val api = ApiClient.api
//
//    suspend fun getCarerDashBoard(): CarerDashBoardResponseModel {
//        return try {
//            val response = api.getCarerDashBoard()
//            if (response.isSuccessful && response.body() != null) {
//                response.body()!!
//            } else {
//                throw Exception("Error: ${response.code()} ${response.message()}")
//            }
//        } catch (e: Exception) {
//            throw Exception("Network Error: ${e.localizedMessage}")
//        }
//    }
//}