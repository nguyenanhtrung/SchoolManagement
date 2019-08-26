package com.nguyenanhtrung.schoolmanagement.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.schoolroom.SchoolRoomDao
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.user.UserDao
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertasks.UserTaskDao
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertype.UserTypeDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.SchoolRoomEntity
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserInfoEntity
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTaskEntity
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTypeEntity

@Database(
    entities = [UserInfoEntity::class,
        UserTaskEntity::class,
        UserTypeEntity::class,
        SchoolRoomEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userTaskDao(): UserTaskDao
    abstract fun userTypeDao(): UserTypeDao
    abstract fun schoolRoomDao(): SchoolRoomDao
}