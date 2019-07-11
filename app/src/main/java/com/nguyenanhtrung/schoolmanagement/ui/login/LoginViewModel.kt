package com.nguyenanhtrung.schoolmanagement.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Event
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.domain.login.LoginUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : BaseActivityViewModel() {

    private val _emailErrorLiveData by lazy {
        MutableLiveData<Int>()
    }
    internal val emailErrorLiveData: LiveData<Int>
        get() = _emailErrorLiveData

    private val _passwordErrorLiveData by lazy {
        MutableLiveData<Int>()
    }
    internal val passwordErrorLiveData: LiveData<Int>
        get() = _passwordErrorLiveData

    private val _showForgotPasswordDialog by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    internal val showForgotPasswordDialog: LiveData<Event<Boolean>>
        get() = _showForgotPasswordDialog

    private val _loginResultLiveData by lazy {
        createApiResultLiveData<Boolean>()
    }
    internal val loginResultLiveData: LiveData<Resource<Boolean>>
        get() = _loginResultLiveData


    internal fun onClickButtonLogin(email: String, password: String) {
        val isLoginValid = Validator.isEmailValid(email, _emailErrorLiveData) && Validator.isPasswordValid(password, _passwordErrorLiveData)
        if (isLoginValid) {
            //call to login
            val loginPair = email to password
            loginUseCase.invoke(viewModelScope,loginPair,_loginResultLiveData)
        }
    }

    internal fun onClickTextForgotPassword() {
        _showForgotPasswordDialog.value = Event(true)
    }

}