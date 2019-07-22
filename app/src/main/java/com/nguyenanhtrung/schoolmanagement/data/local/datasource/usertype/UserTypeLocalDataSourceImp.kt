package com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertype

import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertype.UserTypeDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTypeEntity
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType
import javax.inject.Inject

class UserTypeLocalDataSourceImp @Inject constructor(private val userTypeDao: UserTypeDao) :
    UserTypeLocalDataSource {


    override suspend fun checkUserTypesSaved(): Boolean {
        val index = userTypeDao.checkUserTypesExist()
        return index == 1
    }

    override suspend fun getUserTypes(): List<UserType> {
        val userTypeEntities = userTypeDao.getUserTypes()
        return userTypeEntities.map {
            UserType(it.id, it.name)
        }
    }

    override suspend fun getUserTypeById(userTypeId: String): UserType {
        val userTypeEntity = userTypeDao.getUserType(userTypeId)
        return UserType(userTypeEntity.id, userTypeEntity.name)
    }

    override suspend fun saveUserTypes(userTypeEntities: List<UserTypeEntity>) {
        userTypeDao.insertListData(userTypeEntities)
    }
}