package com.nguyenanhtrung.schoolmanagement.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUserInfoUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class DashboardViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase) : BaseViewModel() {

    private val _userInfoLiveData by lazy {
        createApiResultLiveData<User>()
    }
    internal val userInfoLiveData: LiveData<Resource<User>>
        get() = _userInfoLiveData

    internal fun loadUserInfo() {
        if (_userInfoLiveData.value != null) {
            return
        }
        getUserInfoUseCase.invoke(viewModelScope,Unit, _userInfoLiveData)
    }
}