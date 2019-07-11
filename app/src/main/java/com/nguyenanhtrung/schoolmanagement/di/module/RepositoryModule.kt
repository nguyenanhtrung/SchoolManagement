package com.nguyenanhtrung.schoolmanagement.di.module

import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.LoginRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.LoginRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepositoryImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRemoteDataSource(loginRemoteDataSource: LoginRemoteDataSourceImp): LoginRemoteDataSource {
        return loginRemoteDataSource
    }

    @Singleton
    @Provides
    fun provideLoginRepository(loginRepositoryImp: LoginRepositoryImp): LoginRepository {
        return loginRepositoryImp
    }

}