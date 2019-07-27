package com.nguyenanhtrung.schoolmanagement.ui.accountdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType
import com.nguyenanhtrung.schoolmanagement.domain.usertype.GetUserTypesUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class AccountDetailViewModel @Inject constructor(private val getUserTypesUseCase: GetUserTypesUseCase) : BaseViewModel() {
    internal lateinit var currentUserInfo: User


    private val _currentUserLiveData by lazy {
        MutableLiveData<User>()
    }
    internal val currentUserLiveData: LiveData<User>
        get() = _currentUserLiveData

    private val _userTypesLiveData by lazy {
        createApiResultLiveData<List<UserType>>()
    }
    internal val userTypesLiveData: LiveData<Resource<List<UserType>>>
        get() = _userTypesLiveData


    fun loadUserInfo() {
        _currentUserLiveData.value = currentUserInfo
    }

    fun loadUserTypes() {
        getUserTypesUseCase.invoke(viewModelScope, true, _userTypesLiveData)
    }

}