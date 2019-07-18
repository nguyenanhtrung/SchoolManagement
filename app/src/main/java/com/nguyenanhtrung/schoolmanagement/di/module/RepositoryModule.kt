package com.nguyenanhtrung.schoolmanagement.di.module

import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.login.LoginRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.login.LoginRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task.UserTaskRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task.UserTaskRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user.UserRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user.UserRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepositoryImp
import com.nguyenanhtrung.schoolmanagement.data.repository.task.UserTaskRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.task.UserTaskRepositoryImp
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepositoryImp
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

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(userRemoteDataSource: UserRemoteDataSourceImp): UserRemoteDataSource {
        return userRemoteDataSource
    }

    @Singleton
    @Provides
    fun provideUserRepository(userRepository: UserRepositoryImp): UserRepository {
        return userRepository
    }

    @Singleton
    @Provides
    fun provideUserTaskRemoteDataSource(userTaskRemoteDataSource: UserTaskRemoteDataSourceImp): UserTaskRemoteDataSource {
        return userTaskRemoteDataSource
    }

    @Singleton
    @Provides
    fun provideUserTaskRepository(userTaskRepository: UserTaskRepositoryImp): UserTaskRepository {
        return userTaskRepository
    }

}