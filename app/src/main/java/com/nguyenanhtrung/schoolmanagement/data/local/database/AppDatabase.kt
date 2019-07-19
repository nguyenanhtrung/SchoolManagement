package com.nguyenanhtrung.schoolmanagement.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.user.UserDao
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertasks.UserTaskDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserInfoEntity
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTaskEntity

@Database(entities = [UserInfoEntity::class, UserTaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userTaskDao(): UserTaskDao
}