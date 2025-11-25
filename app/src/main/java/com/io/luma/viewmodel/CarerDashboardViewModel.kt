package com.io.luma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.CarerDashBoardResponseModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.network.Resource
import com.io.luma.respositry.CarerDashboardRespositry
import com.io.luma.respositry.CarerSignupRespositry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class CarerDashboardViewModel : ViewModel() {
    private val repository = CarerDashboardRespositry()

    // Make this public so it can be collected in the Composable
    val createUser = MutableStateFlow<Resource<CarerDashBoardResponseModel>?>(null)

    fun getCarerDashBoard() {
        viewModelScope.launch {
            createUser.value = Resource.Loading()
            createUser.value = repository.getCarerDashBoard()
        }
    }
}

//class CarerDashboardViewModel : ViewModel(){
//
//    private val repository = CarerDashboardRespositry()
//    private val _createUser = MutableStateFlow<Resource<CarerDashBoardResponseModel>?>(null)
//
//
//    fun getCarerDashBoard() {
//        viewModelScope.launch {
//            _createUser.value = Resource.Loading()
//            _createUser.value = repository.getCarerDashBoard()
//        }
//    }
//}