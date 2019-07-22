package com.nguyenanhtrung.schoolmanagement.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nguyenanhtrung.schoolmanagement.data.local.database.AppDatabase
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.user.UserDao
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertasks.UserTaskDao
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertype.UserTypeDao
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "school_management").build()
    }

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Singleton
    @Provides
    fun provideUserTasksDao(appDatabase: AppDatabase): UserTaskDao = appDatabase.userTaskDao()

    @Singleton
    @Provides
    fun provideUserTypeDao(appDatabase: AppDatabase): UserTypeDao = appDatabase.userTypeDao()

}