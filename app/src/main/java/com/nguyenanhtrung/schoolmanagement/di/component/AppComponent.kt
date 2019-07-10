package com.nguyenanhtrung.schoolmanagement.di.component

import android.app.Application
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.di.module.DatabaseModule
import com.nguyenanhtrung.schoolmanagement.di.module.NetworkModule
import com.nguyenanhtrung.schoolmanagement.di.module.RepositoryModule
import com.nguyenanhtrung.schoolmanagement.di.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class]
)
interface AppComponent {
    fun inject(application: Application)
}