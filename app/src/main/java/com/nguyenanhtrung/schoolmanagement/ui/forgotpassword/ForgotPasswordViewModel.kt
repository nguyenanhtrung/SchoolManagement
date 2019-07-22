package com.nguyenanhtrung.schoolmanagement.ui.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.Event
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.domain.sendresetpassword.SendResetPasswordUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(private val sendResetPasswordUseCase: SendResetPasswordUseCase) :
    BaseViewModel() {

    private val _dismissDialogLiveData by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    internal val dismissDialogLiveData: LiveData<Event<Boolean>>
        get() = _dismissDialogLiveData

    internal fun onClickButtonClose() {
        _dismissDialogLiveData.value = Event(true)
    }

    private val _sendPassLiveData by lazy {
        createApiResultLiveData<Int>()
    }
    internal val sendPassLiveData: LiveData<Resource<Int>>
        get() = _sendPassLiveData

    internal fun onClickButtonConfirm(email: String) {
        val isEmailValid = Validator.isEmailValid(email, _errorLiveData)
        if (isEmailValid) {
            sendResetPasswordUseCase.invoke(viewModelScope, email, _sendPassLiveData)
        }
    }
}