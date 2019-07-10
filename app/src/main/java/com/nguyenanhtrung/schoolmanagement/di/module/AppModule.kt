package com.nguyenanhtrung.schoolmanagement.di.module

import android.app.Application
import android.content.Context
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @ApplicationContext
    fun getContext(): Context {
        return application.applicationContext
    }
}