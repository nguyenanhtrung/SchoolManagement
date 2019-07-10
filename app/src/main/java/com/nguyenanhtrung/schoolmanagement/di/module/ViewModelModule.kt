package com.nguyenanhtrung.schoolmanagement.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nguyenanhtrung.schoolmanagement.ui.login.LoginViewModel
import com.nguyenanhtrung.schoolmanagement.util.AppViewModelFactory
import com.nguyenanhtrung.schoolmanagement.util.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(appViewModelFactory: AppViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel) : ViewModel
}