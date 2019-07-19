package com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertasks

import androidx.room.Dao
import androidx.room.Query
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.base.BaseDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTaskEntity

@Dao
interface UserTaskDao : BaseDao<UserTaskEntity> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM UserTask WHERE user_type_id LIKE :userTypeId LIMIT 1)")
    suspend fun checkUserTasksSaved(userTypeId: String): Int

    @Query(value = "SELECT * FROM UserTask WHERE user_type_id LIKE :userTypeId")
    suspend fun getUserTasks(userTypeId: String): List<UserTaskEntity>
}