package com.io.luma.respositry

import com.io.luma.model.NotificationResponseModel
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource

class NotificationRepository {
    private val api = ApiClient.api

    suspend fun getNotifications(
        page: Int = 1,
        limit: Int = 20,
        unreadOnly: Boolean = false
    ): Resource<NotificationResponseModel> {
        return try {
            val response = api.getNotifications(page, limit, unreadOnly)
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