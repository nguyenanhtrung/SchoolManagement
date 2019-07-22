package com.nguyenanhtrung.schoolmanagement.ui.createaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType
import com.nguyenanhtrung.schoolmanagement.domain.usertype.GetUserTypesUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class CreateAccountViewModel @Inject constructor(private val getUserTypesUseCase: GetUserTypesUseCase) : BaseViewModel() {

    private val _userTypesLiveData by lazy {
        createApiResultLiveData<List<UserType>>()
    }
    internal val userTypesLiveData: LiveData<Resource<List<UserType>>>
        get() = _userTypesLiveData


    internal fun loadUserTypes() {
        if (_userTypesLiveData.value?.data != null) {
            return
        }
        getUserTypesUseCase.invoke(viewModelScope, true, _userTypesLiveData)
    }
}