package com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertype

import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTypeEntity
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType

interface UserTypeLocalDataSource {

    suspend fun checkUserTypesSaved(): Boolean
    suspend fun getUserTypes(): List<UserType>
    suspend fun getUserTypeById(userTypeId: String): UserType
    suspend fun saveUserTypes(userTypeEntities: List<UserTypeEntity>)
}