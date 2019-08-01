package com.nguyenanhtrung.schoolmanagement.di.module

import com.nguyenanhtrung.schoolmanagement.data.local.datasource.profile.ProfileLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.profile.ProfileLocalDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.user.UserLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.user.UserLocalDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertasks.UserTasksLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertasks.UserTasksLocalDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertype.UserTypeLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertype.UserTypeLocalDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.login.LoginRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.login.LoginRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile.ProfileRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile.ProfileRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task.UserTaskRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task.UserTaskRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user.UserRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user.UserRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.userid.UserIdRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.userid.UserIdRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.usertype.UserTypeRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.usertype.UserTypeRemoteDataSourceImp
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepositoryImp
import com.nguyenanhtrung.schoolmanagement.data.repository.profile.ProfileRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.profile.ProfileRepositoryImp
import com.nguyenanhtrung.schoolmanagement.data.repository.task.UserTaskRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.task.UserTaskRepositoryImp
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepositoryImp
import com.nguyenanhtrung.schoolmanagement.data.repository.userid.UserIdRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.userid.UserIdRepositoryImp
import com.nguyenanhtrung.schoolmanagement.data.repository.usertype.UserTypeRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.usertype.UserTypeRepositoryImp
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
    fun provideUserLocalDataSource(userLocalDataSourceImp: UserLocalDataSourceImp): UserLocalDataSource {
        return userLocalDataSourceImp
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
    fun provideUserTaskLocalDataSource(userTasksLocalDataSourceImp: UserTasksLocalDataSourceImp): UserTasksLocalDataSource {
        return userTasksLocalDataSourceImp
    }

    @Singleton
    @Provides
    fun provideUserTaskRepository(userTaskRepository: UserTaskRepositoryImp): UserTaskRepository {
        return userTaskRepository
    }

    @Singleton
    @Provides
    fun provideUserTypeLocalDataSource(userTypeLocalDataSourceImp: UserTypeLocalDataSourceImp): UserTypeLocalDataSource {
        return userTypeLocalDataSourceImp
    }

    @Singleton
    @Provides
    fun provideUserTypeRemoteDataSource(userTypeRemoteDataSourceImp: UserTypeRemoteDataSourceImp): UserTypeRemoteDataSource {
        return userTypeRemoteDataSourceImp
    }

    @Singleton
    @Provides
    fun provideUserTypeRepository(userTypeRepository: UserTypeRepositoryImp): UserTypeRepository {
        return userTypeRepository
    }

    @Singleton
    @Provides
    fun provideUserIdRemoteDataSource(userIdRemoteDataSource: UserIdRemoteDataSourceImp): UserIdRemoteDataSource {
        return userIdRemoteDataSource
    }

    @Singleton
    @Provides
    fun provideUserIdRepository(userIdRepositoryImp: UserIdRepositoryImp): UserIdRepository {
        return userIdRepositoryImp
    }

    @Singleton
    @Provides
    fun provideProfileLocalDataSource(profileLocalDataSourceImp: ProfileLocalDataSourceImp): ProfileLocalDataSource {
        return profileLocalDataSourceImp
    }

    @Singleton
    @Provides
    fun provideProfileRemoteDataSource(profileRemoteDataSource: ProfileRemoteDataSourceImp): ProfileRemoteDataSource {
        return profileRemoteDataSource
    }

    @Singleton
    @Provides
    fun provideProfileRepository(profileRepository: ProfileRepositoryImp): ProfileRepository {
        return profileRepository
    }
}