package com.nguyenanhtrung.schoolmanagement.data.local.database.dao.base

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface BaseDao<T> {

    @Insert
    suspend fun insertData(data: T)

    @Insert
    suspend fun insertListData(listData: List<T>)
}