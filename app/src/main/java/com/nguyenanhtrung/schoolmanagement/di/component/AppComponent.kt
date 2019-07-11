package com.nguyenanhtrung.schoolmanagement.di.component

import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.di.module.*
import com.nguyenanhtrung.schoolmanagement.ui.forgotpassword.DialogForgotPasswordFragment
import com.nguyenanhtrung.schoolmanagement.ui.login.LoginActivity
import com.nguyenanhtrung.schoolmanagement.ui.main.MainActivity
import com.nguyenanhtrung.schoolmanagement.ui.splash.SplashActivity
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
    fun inject(forgotPasswordFragment: DialogForgotPasswordFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(splashActivity: SplashActivity)
}