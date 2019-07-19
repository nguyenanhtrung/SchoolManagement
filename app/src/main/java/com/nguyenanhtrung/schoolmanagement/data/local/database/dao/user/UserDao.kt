package com.nguyenanhtrung.schoolmanagement.data.local.database.dao.user

import androidx.room.Dao
import androidx.room.Query
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.base.BaseDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserInfoEntity

@Dao
interface UserDao : BaseDao<UserInfoEntity> {

    @Query(value = "SELECT * FROM User WHERE id LIKE :userId")
    suspend fun getUserInfo(userId: String): UserInfoEntity

    @Query(value = "SELECT EXISTS(SELECT 1 FROM User WHERE id LIKE (:userId)  LIMIT 1)")
    suspend fun checkExistUserInfo(userId: String): Int

}