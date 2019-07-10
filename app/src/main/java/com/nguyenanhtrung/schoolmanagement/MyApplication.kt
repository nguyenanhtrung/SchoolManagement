package com.nguyenanhtrung.schoolmanagement

import android.app.Application
import com.google.firebase.FirebaseApp
import com.nguyenanhtrung.schoolmanagement.di.component.AppComponent
import com.nguyenanhtrung.schoolmanagement.di.component.DaggerAppComponent
import com.nguyenanhtrung.schoolmanagement.di.module.AppModule
import com.nguyenanhtrung.schoolmanagement.di.module.DatabaseModule
import com.nguyenanhtrung.schoolmanagement.di.module.NetworkModule
import com.nguyenanhtrung.schoolmanagement.di.module.ViewModelModule

class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .databaseModule(DatabaseModule())
            .networkModule(NetworkModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        appComponent.inject(this)
    }
}