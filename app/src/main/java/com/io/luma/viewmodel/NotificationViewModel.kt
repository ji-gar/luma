package com.io.luma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.NotificationResponseModel
import com.io.luma.network.Resource
import com.io.luma.respositry.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {
    private val repository = NotificationRepository()

    val notifications = MutableStateFlow<Resource<NotificationResponseModel>?>(null)

    private var currentPage = 1
    private val limit = 20

    fun getNotifications(unreadOnly: Boolean = false, loadMore: Boolean = false) {
        viewModelScope.launch {
            if (loadMore) {
                currentPage++
            } else {
                currentPage = 1
            }

            if (!loadMore) {
                notifications.value = Resource.Loading()
            }

            notifications.value = repository.getNotifications(currentPage, limit, unreadOnly)
        }
    }

    fun refreshNotifications() {
        getNotifications(unreadOnly = false, loadMore = false)
    }
}
