package com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertype

import androidx.room.Dao
import androidx.room.Query
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.base.BaseDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTypeEntity

@Dao
interface UserTypeDao : BaseDao<UserTypeEntity> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM UserType LIMIT 1)")
    suspend fun checkUserTypesExist(): Int

    @Query(value = "SELECT * FROM UserType")
    suspend fun getUserTypes(): List<UserTypeEntity>

    @Query(value = "SELECT * FROM UserType WHERE id LIKE :userTypeId")
    suspend fun getUserType(userTypeId: String): UserTypeEntity
}