package com.nguyenanhtrung.schoolmanagement.di.module

import androidx.lifecycle.ViewModelProvider
import com.nguyenanhtrung.schoolmanagement.util.AppViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(appViewModelFactory: AppViewModelFactory) : ViewModelProvider.Factory
}