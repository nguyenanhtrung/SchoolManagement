package com.nguyenanhtrung.schoolmanagement.di.component

import android.app.Application
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.di.module.*
import com.nguyenanhtrung.schoolmanagement.ui.login.LoginActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class,
        AppModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class]
)
interface AppComponent {
    fun inject(application: MyApplication)
    fun inject(loginActivity: LoginActivity)
}