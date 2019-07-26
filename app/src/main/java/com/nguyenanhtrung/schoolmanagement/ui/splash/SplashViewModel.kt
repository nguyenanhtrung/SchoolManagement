package com.nguyenanhtrung.schoolmanagement.ui.splash

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.login.CheckAlreadyLoginUseCase
import com.nguyenanhtrung.schoolmanagement.domain.usertype.GetUserTypesUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val checkAlreadyLoginUseCase: CheckAlreadyLoginUseCase,
                                          private val getUserTypesUseCase: GetUserTypesUseCase) :
    BaseActivityViewModel() {

    private val _checkLoginLiveData by lazy {
        createApiResultLiveData<LoginState>()
    }
    internal val checkLoginLiveData: LiveData<Resource<LoginState>>
        get() = _checkLoginLiveData

    private val _userTypeLiveData by lazy {
        createApiResultLiveData<List<UserType>>()
    }
    internal val userTypeLiveData: LiveData<Resource<List<UserType>>>
        get() = _userTypeLiveData

    private val _navigateLoginScreen by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    internal val navigateLoginScreen: LiveData<Event<Boolean>>
        get() = _navigateLoginScreen

    private val _navigateMainScreen by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    internal val navigateMainScreen: LiveData<Event<Boolean>>
        get() = _navigateMainScreen


    init {
        checkAlreadyLoginUseCase.invoke(viewModelScope, Unit, _checkLoginLiveData)
    }



    internal fun onCheckLoginState(loginState: LoginState?) {
        loginState?.let {
            if (it == LoginState.FIRST_LOGIN) {
                //open login screen
                _navigateLoginScreen.value = Event(true)
            } else if (it == LoginState.ALREADY_LOGIN) {
                getUserTypesUseCase.invoke(viewModelScope,true, _userTypeLiveData)
            }
        }
    }

    internal fun onCompletedLoadUserTypes() {
        _checkLoginLiveData.value?.data?.let {
            if (it == LoginState.ALREADY_LOGIN) {
                _navigateMainScreen.value = Event(true)
            }
        }
    }

    internal fun onClickButtonRetry() {
        showError(ErrorState.Empty)
        checkAlreadyLoginUseCase.invoke(viewModelScope, Unit, _checkLoginLiveData)
    }

    override fun handleErrorState(resource: Resource<*>) {
        showError(ErrorState.NoAction(resource.error))
    }

}