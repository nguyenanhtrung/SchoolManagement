package com.nguyenanhtrung.schoolmanagement.ui.splash

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.LoginState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.domain.login.CheckAlreadyLoginUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(checkAlreadyLoginUseCase: CheckAlreadyLoginUseCase) :
    BaseActivityViewModel() {

    private val _checkLoginLiveData by lazy {
        MutableLiveData<Resource<LoginState>>()
    }
    internal val checkLoginLiveData: LiveData<Resource<LoginState>>
        get() = _checkLoginLiveData

    init {
        checkAlreadyLoginUseCase.invoke(viewModelScope, Unit, _checkLoginLiveData)
    }

    internal fun onCheckLoginState(loginState: LoginState?) {
        loginState?.let {
            if (it == LoginState.FIRST_LOGIN) {
                //open login screen
            } else if (it == LoginState.ALREADY_LOGIN) {
                //open main screen
            }
        }
    }

}