package com.nguyenanhtrung.schoolmanagement.di.module

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

}